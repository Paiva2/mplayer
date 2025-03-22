package org.com.mplayer.player.domain.core.usecase.insertMusic;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.core.usecase.common.exception.InvalidContentTypeException;
import org.com.mplayer.player.domain.core.usecase.insertMusic.exception.MusicAlreadyAddedByUserException;
import org.com.mplayer.player.domain.ports.in.usecase.InsertMusicUsecasePort;
import org.com.mplayer.player.domain.ports.out.data.CollectionDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.LyricDataProviderPort;
import org.com.mplayer.player.domain.ports.out.data.MusicDataProviderPort;
import org.com.mplayer.player.domain.ports.out.external.CoverExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.LyricsExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileDTO;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.com.mplayer.player.infra.annotations.Usecase;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Usecase
@AllArgsConstructor
public class InsertMusicUsecase implements InsertMusicUsecasePort {
    private final static String FILES_IMAGES_DESTINATION = "mplayer-images";
    private final static String FILES_TRACKS_DESTINATION = "mplayer-tracks";

    private final MusicDataProviderPort musicDataProviderPort;
    private final CollectionDataProviderPort collectionDataProviderPort;
    private final LyricDataProviderPort lyricDataProviderPort;

    private final LyricsExternalIntegrationPort lyricsExternalIntegrationPort;
    private final CoverExternalIntegrationPort coverExternalIntegrationPort;
    private final UserExternalIntegrationPort userExternalIntegrationPort;
    private final FileExternalIntegrationPort fileExternalIntegrationPort;

    private final FileUtilsPort fileUtilsPort;

    @Override
    @Transactional
    public void execute(MultipartFile musicFile) {
        FindUserExternalProfileDTO user = findUser();

        String contentType = fileUtilsPort.getContentType(musicFile);

        if (contentType == null || contentType.isEmpty()) {
            throw new InvalidContentTypeException("Invalid content type. Valid are: " + Arrays.toString(EFileType.values()));
        } else {
            validateContentType(contentType.toUpperCase());
        }

        Map<String, Object> metadata = fileUtilsPort.readFileMetadata(musicFile);
        checkUserAlreadyAddedMusic(user.getId().toString(), metadata, contentType);

        Map<String, String> fileUploaded = uploadMusic(user.getId().toString(), musicFile, contentType);

        Music music = fillMusic(user.getId().toString(), metadata, contentType, musicFile.getOriginalFilename(), fileUploaded);
        music = persistMusic(music);

        Lyric musicLyric = fillMusicLyric(music);

        if (musicLyric != null) {
            persistLyric(musicLyric);
        }
    }

    private FindUserExternalProfileDTO findUser() {
        return userExternalIntegrationPort.findByExternalId();
    }

    private void validateContentType(String contentType) {
        try {
            EFileType.valueOf(contentType);
        } catch (Exception e) {
            throw new InvalidContentTypeException("Invalid content type. Valid are: " + Arrays.toString(EFileType.values()));
        }
    }

    private void checkUserAlreadyAddedMusic(String externalUserId, Map<String, Object> metadata, String contentType) {
        String trackTitle = (String) metadata.get("title");
        String artist = (String) metadata.get("artist");

        if (trackTitle == null || artist == null) return;

        Optional<Music> music = findMusic(externalUserId, artist, trackTitle, contentType.toUpperCase());

        if (music.isPresent()) {
            throw new MusicAlreadyAddedByUserException("User already has an track from this artist with the title: " + trackTitle + " added!");
        }
    }

    private Optional<Music> findMusic(String externalUserId, String artist, String trackTitle, String contentType) {
        return musicDataProviderPort.findMusicByUserAndArtistAndTrack(externalUserId, artist, trackTitle, contentType);
    }

    private Music fillMusic(String userId, Map<String, Object> metadata, String contentType, String fileName, Map<String, String> fileUploaded) {
        String albumName = "unknown";
        String trackTitle = "unknown";
        String artist = "unknown";
        String composer = "unknown";
        byte[] cover = (byte[]) metadata.get("cover");
        String genreFormatted = "unknown";
        String releaseYear = "unknown";

        if (metadata.get("genre") != null) {
            genreFormatted = ((String) metadata.get("genre")).replaceAll(" / ", ";").replaceAll("/", ";").replaceAll(",", ";").replaceAll(", ", ";");
        }

        if (metadata.get("year") != null && !((String) metadata.get("year")).isEmpty()) {
            releaseYear = (String) metadata.get("year");
        }

        if (metadata.get("composer") != null && !((String) metadata.get("composer")).isEmpty()) {
            composer = (String) metadata.get("composer");
        }

        if (metadata.get("artist") != null && !((String) metadata.get("artist")).isEmpty()) {
            artist = (String) metadata.get("artist");
        }

        if (metadata.get("title") != null && !((String) metadata.get("title")).isEmpty()) {
            trackTitle = (String) metadata.get("title");
        }

        if (metadata.get("album") != null && !((String) metadata.get("album")).isEmpty()) {
            albumName = (String) metadata.get("album");
        }

        return Music.builder()
            .title(trackTitle)
            .artist(artist)
            .genre(genreFormatted)
            .composer(composer)
            .releaseYear(releaseYear)
            .coverUrl(cover == null ? findCoverImageUrl(artist, trackTitle) : uploadCoverImage(userId, cover, fileName))
            .durationSeconds((long) (int) metadata.get("length"))
            .fileType(EFileType.valueOf(contentType.toUpperCase()))
            .externalUserId(userId)
            .collection(handleCollection(userId, albumName, artist))
            .repositoryUrl(fileUploaded.get("url"))
            .externalIdentification(fileUploaded.get("id"))
            .build();
    }

    private Music persistMusic(Music music) {
        return musicDataProviderPort.persist(music);
    }

    private Lyric fillMusicLyric(Music music) {
        if (music.getArtist().equals("unknown") || music.getTitle().equals("unknown")) return null;

        Optional<Lyric> lyricExistent = findLyric(music.getArtist(), music.getTitle());

        Lyric newLyric = Lyric.builder()
            .music(music)
            .build();

        if (lyricExistent.isPresent()) {
            newLyric.setLyric(lyricExistent.get().getLyric());

            return newLyric;
        }

        String externalLyric = findLyricExternal(music.getArtist(), music.getTitle());

        if (externalLyric != null && !externalLyric.isEmpty()) {
            newLyric.setLyric(externalLyric);
        } else {
            newLyric.setLyric("");
        }

        return newLyric;
    }

    private void persistLyric(Lyric lyric) {
        lyricDataProviderPort.persist(lyric);
    }

    private Map<String, String> uploadMusic(String externalUserId, MultipartFile multipartFile, String contentType) {
        return fileExternalIntegrationPort.insertFile(multipartFile, externalUserId, FILES_TRACKS_DESTINATION, contentType);
    }

    private String findCoverImageUrl(String artist, String trackTitle) {
        return coverExternalIntegrationPort.findTrackCoverImageUrlByArtistAndTrack(artist, trackTitle);
    }

    private String findAlbumCoverImageUrl(String artist, String albumName) {
        return coverExternalIntegrationPort.findAlbumCoverImageUrlByArtist(artist, albumName);
    }

    private String uploadCoverImage(String externalUserId, byte[] coverBytes, String fileName) {
        String coverImageIdentification = externalUserId.concat("_").concat("cover_").concat(fileName);

        Map<String, String> uploadedCover = fileExternalIntegrationPort.insertFile(coverBytes, coverImageIdentification, FILES_IMAGES_DESTINATION, "jpg");

        return uploadedCover.get("url");
    }

    private Optional<Lyric> findLyric(String artist, String trackTitle) {
        return lyricDataProviderPort.findLyricByArtistAndMusicTitle(trackTitle, artist);
    }

    private String findLyricExternal(String artist, String trackTitle) {
        return lyricsExternalIntegrationPort.findLyricByArtistAndTrack(artist, trackTitle);
    }

    private Collection handleCollection(String externalUserId, String albumName, String artist) {
        if (albumName.equals("unknown") || artist.equals("unknown")) return null;

        Optional<Collection> collection = findCollection(externalUserId, albumName, artist);

        if (collection.isPresent()) {
            return collection.get();
        }

        Collection newCollection = Collection.builder()
            .artist(artist)
            .title(albumName)
            .externalUserId(externalUserId)
            .imageUrl(findAlbumCoverImageUrl(artist, albumName))
            .build();

        newCollection = persistNewCollection(newCollection);

        return newCollection;
    }

    private Optional<Collection> findCollection(String externalUserId, String album, String artist) {
        return collectionDataProviderPort.findCollectionByUserAndAlbumName(externalUserId, album, artist);
    }

    private Collection persistNewCollection(Collection collection) {
        return collectionDataProviderPort.persistCollection(collection);
    }
}
