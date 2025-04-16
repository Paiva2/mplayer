package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMusicOutputPort {
    private Long id;
    private String title;
    private String artist;
    private String genre;
    private String releaseYear;
    private String coverUrl;
    private String composer;
    private String durationSeconds;
    private EFileType fileType;
    private Date createdAt;
    private CollectionDTO collection;
    private LyricsDTO lyrics;

    public GetMusicOutputPort(Music music) {
        this.id = music.getId();
        this.title = music.getTitle();
        this.artist = music.getArtist();
        this.genre = music.getGenre();
        this.releaseYear = music.getReleaseYear();
        this.coverUrl = music.getCoverUrl();
        this.composer = music.getComposer();
        this.durationSeconds = convertSecondsToHourMinuteSecond(music.getDurationSeconds());
        this.fileType = music.getFileType();
        this.createdAt = music.getCreatedAt();
        this.collection = music.getCollection() != null ? CollectionDTO.builder()
            .id(music.getCollection().getId())
            .title(music.getCollection().getTitle())
            .artist(music.getCollection().getArtist())
            .imageUrl(music.getCollection().getImageUrl())
            .build() : null;
        this.lyrics = music.getLyric() != null ? LyricsDTO.builder()
            .id(music.getLyric().getId())
            .lyric(music.getLyric().getLyric())
            .build() : null;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollectionDTO {
        private Long id;
        private String title;
        private String artist;
        private String imageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LyricsDTO {
        private Long id;
        private String lyric;
    }

    private String convertSecondsToHourMinuteSecond(long seconds) {
        StringBuilder builder = new StringBuilder();

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if (hours > 0) {
            if (hours < 10) {
                builder.append("0").append(hours).append(":");
            } else {
                builder.append(hours).append(":");
            }
        }

        if (minutes < 10) {
            builder.append("0").append(minutes).append(":");
        } else {
            builder.append(minutes).append(":");
        }

        if (seconds < 10) {
            builder.append("0").append(seconds);
        } else {
            builder.append(seconds);
        }

        return builder.toString();
    }
}
