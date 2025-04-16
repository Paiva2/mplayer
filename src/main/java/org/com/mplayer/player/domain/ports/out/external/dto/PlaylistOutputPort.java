package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistOutputPort {
    private Long id;
    private String name;
    private String coverUrl;
    private Boolean visiblePublic;
    private Date createdAt;
    private String externalUserId;
    private Long musicsQuantity;

    public PlaylistOutputPort(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.coverUrl = playlist.getCoverUrl();
        this.visiblePublic = playlist.getVisiblePublic();
        this.createdAt = playlist.getCreatedAt();
        this.externalUserId = playlist.getExternalUserId();
        this.musicsQuantity = playlist.getPlaylistMusics().isEmpty() ? 0 : (long) playlist.getPlaylistMusics().size();
    }
}
