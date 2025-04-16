package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.MusicQueue;
import org.com.mplayer.player.domain.core.enums.EFileType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListQueueMusicsOutputPort {
    private Long queueId;
    private List<MusicQueueDTO> musics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicQueueDTO {
        private Long musicId;
        private String title;
        private String artist;
        private String duration;
        private Long durationSeconds;
        private EFileType fileType;
        private String coverUrl;
        private Integer position;
        private CollectionDTO collection;

        public MusicQueueDTO(MusicQueue musicQueue) {
            this.musicId = musicQueue.getMusic().getId();
            this.title = musicQueue.getMusic().getTitle();
            this.artist = musicQueue.getMusic().getArtist();
            this.duration = convertSecondsToHourMinuteSecond(musicQueue.getMusic().getDurationSeconds());
            this.durationSeconds = musicQueue.getMusic().getDurationSeconds();
            this.fileType = musicQueue.getMusic().getFileType();
            this.coverUrl = musicQueue.getMusic().getCoverUrl();
            this.position = musicQueue.getPosition();
            this.collection = musicQueue.getMusic().getCollection() == null ? null : CollectionDTO.builder()
                .id(musicQueue.getMusic().getCollection().getId())
                .title(musicQueue.getMusic().getCollection().getTitle())
                .artist(musicQueue.getMusic().getCollection().getArtist())
                .imageUrl(musicQueue.getMusic().getCollection().getImageUrl())
                .build();
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
}
