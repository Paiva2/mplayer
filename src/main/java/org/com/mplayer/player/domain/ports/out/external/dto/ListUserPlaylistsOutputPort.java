package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Playlist;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListUserPlaylistsOutputPort {
    private Long id;
    private String name;
    private String coverUrl;
    private Date createdAt;
    private String externalUserId;
    private Integer musicsQuantity;

    public ListUserPlaylistsOutputPort(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.coverUrl = playlist.getCoverUrl();
        this.createdAt = playlist.getCreatedAt();
        this.externalUserId = playlist.getExternalUserId();
        this.musicsQuantity = playlist.getPlaylistMusics().size();
    }
}
