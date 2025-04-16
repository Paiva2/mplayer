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
public class ListMusicFilesOutputPort {
    private int page;
    private int size;
    private long totalMusics;
    private int totalPages;
    private List<MusicDTO> musics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicDTO {
        private Long id;
        private String title;
        private String artist;
        private String genre;
        private String releaseYear;
        private String coverUrl;
        private String composer;
        private Long durationSeconds;
        private EFileType fileType;
        private Date createdAt;
        private CollectionDTO collection;

        public MusicDTO(Music music) {
            this.id = music.getId();
            this.title = music.getTitle();
            this.artist = music.getArtist();
            this.genre = music.getGenre();
            this.releaseYear = music.getReleaseYear();
            this.coverUrl = music.getCoverUrl();
            this.composer = music.getComposer();
            this.durationSeconds = music.getDurationSeconds();
            this.fileType = music.getFileType();
            this.createdAt = music.getCreatedAt();
            this.collection = music.getCollection() != null ? CollectionDTO.builder()
                .id(music.getCollection().getId())
                .title(music.getCollection().getTitle())
                .artist(music.getCollection().getArtist())
                .imageUrl(music.getCollection().getImageUrl())
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
    }
}
