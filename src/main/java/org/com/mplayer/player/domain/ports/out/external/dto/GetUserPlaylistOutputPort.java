package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.enums.EFileType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserPlaylistOutputPort {
    private Long id;
    private String name;
    private String coverUrl;
    private Boolean visiblePublic;
    private Date createdAt;
    private Date updatedAt;
    private List<PlaylistMusicOutput> musics;
    private PaginationOutput pagination;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationOutput {
        int page;
        int size;
        int totalPages;
        long totalItems;
        boolean isLast;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaylistMusicOutput {
        private Long musicId;
        private Integer position;
        private String title;
        private String artist;
        private String coverUrl;
        private Long durationSeconds;
        private EFileType fileType;
        private Date createdAt;
        private CollectionOutput collection;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CollectionOutput {
            private Long id;
            private String title;
        }
    }
}
