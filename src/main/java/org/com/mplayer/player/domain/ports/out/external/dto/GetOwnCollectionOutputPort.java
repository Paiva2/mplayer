package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Music;
import org.com.mplayer.player.domain.core.enums.EFileType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOwnCollectionOutputPort {
    private Long id;
    private String title;
    private String artist;
    private String imageUrl;
    private Date createdAt;
    private List<MusicOutput> musics;

    @Data
    @Builder
    @AllArgsConstructor
    public static class MusicOutput {
        private Long id;
        private String title;
        private String artist;
        private String coverUrl;
        private Long durationSeconds;
        private EFileType fileType;
        private Date createdAt;

        public MusicOutput(Music music) {
            this.id = music.getId();
            this.title = music.getTitle();
            this.artist = music.getArtist();
            this.coverUrl = music.getCoverUrl();
            this.durationSeconds = music.getDurationSeconds();
            this.fileType = music.getFileType();
            this.createdAt = music.getCreatedAt();
        }
    }
}
