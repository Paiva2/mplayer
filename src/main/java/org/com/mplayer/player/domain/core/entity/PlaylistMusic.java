package org.com.mplayer.player.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistMusic {
    private KeyId id;
    private Integer position;
    private Date createdAt;
    private Date updatedAt;

    private Playlist playlist;
    private Music music;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long playlistId;
        private Long musicId;
    }
}
