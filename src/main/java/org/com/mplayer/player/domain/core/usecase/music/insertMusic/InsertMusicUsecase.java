package org.com.mplayer.player.domain.core.usecase.music.insertMusic;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;
import org.com.mplayer.player.domain.core.entity.Lyric;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.com.mplayer.player.domain.core.usecase.common.exception.InvalidContentTypeException;
import org.com.mplayer.player.domain.core.usecase.music.insertMusic.exception.MusicAlreadyAddedByUserException;
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
import java.util.UUID;

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

        String originalFileNameNoExtension = fileUtilsPort.fileNameWithoutExtension(musicFile.getOriginalFilename());
        String fileExternalId = generateExternalId(user.getId().toString(), originalFileNameNoExtension);

        String fileExternalIdMusic = fileExternalId.concat("_track").concat(".").concat(contentType);
        String fileUploadedUrl = "";//uploadMusic(musicFile, fileExternalIdMusic, contentType);

        Music music = fillMusic(originalFileNameNoExtension, user.getId().toString(), metadata, contentType, fileUploadedUrl, fileExternalId);
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

    private String generateExternalId(String externalUserId, String originalFileName) {
        return externalUserId.concat("_").concat(UUID.randomUUID().toString()).concat("_").concat(originalFileName);
    }

    private Optional<Music> findMusic(String externalUserId, String artist, String trackTitle, String contentType) {
        return musicDataProviderPort.findMusicByUserAndArtistAndTrack(externalUserId, artist, trackTitle, contentType);
    }

    private Music fillMusic(String originalFileName, String userId, Map<String, Object> metadata, String contentType, String fileUploadedUrl, String fileExternalId) {
        String albumName = "unknown";
        String trackTitle = "unknown";
        String artist = "unknown";
        String composer = "unknown";
        byte[] cover = metadata.get("cover") != null ? (byte[]) metadata.get("cover") : null;
        String genreFormatted = "unknown";
        String releaseYear = "unknown";
        String coverContentType = "unknown";

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
        } else {
            trackTitle = originalFileName;
        }

        if (metadata.get("album") != null && !((String) metadata.get("album")).isEmpty()) {
            albumName = (String) metadata.get("album");
        }

        if (metadata.get("cover_content_type") != null && !((String) metadata.get("cover_content_type")).isEmpty()) {
            coverContentType = (String) metadata.get("cover_content_type");
        }

        return Music.builder()
            .title(trackTitle)
            .artist(artist)
            .genre(genreFormatted)
            .composer(composer)
            .releaseYear(releaseYear)
            .coverUrl(cover == null ? findCoverImageUrl(artist, trackTitle) : uploadCoverImage(fileExternalId, cover, coverContentType))
            .durationSeconds(metadata.get("length") != null ? (long) (int) metadata.get("length") : 0)
            .fileType(EFileType.valueOf(contentType.toUpperCase()))
            .externalUserId(userId)
            .collection(handleCollection(userId, albumName, artist))
            .repositoryUrl(fileUploadedUrl)
            .coverContentType(coverContentType)
            .externalIdentification(fileExternalId)
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

    private String uploadMusic(MultipartFile multipartFile, String fileName, String contentType) {
        return fileExternalIntegrationPort.insertFile(multipartFile, fileName, FILES_TRACKS_DESTINATION, contentType);
    }

    private String findCoverImageUrl(String artist, String trackTitle) {
        if (artist.equals("unknown") || trackTitle == null || trackTitle.equals("unknown")) return null;

        return coverExternalIntegrationPort.findTrackCoverImageUrlByArtistAndTrack(artist, trackTitle);
    }

    private String findAlbumCoverImageUrl(String artist, String albumName) {
        return coverExternalIntegrationPort.findAlbumCoverImageUrlByArtist(artist, albumName);
    }

    private String uploadCoverImage(String fileExternalId, byte[] coverBytes, String coverContentType) {
        if (coverContentType.equals("unknown")) return null;

        String fileName = fileExternalId.concat("_cover").concat(".").concat(coverContentType);

        return fileExternalIntegrationPort.insertFile(coverBytes, fileName, FILES_IMAGES_DESTINATION, coverContentType);
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
