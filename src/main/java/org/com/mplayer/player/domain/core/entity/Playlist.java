package org.com.mplayer.player.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    private Long id;
    private String name;
    private String coverUrl;
    private Boolean visiblePublic;
    private Date createdAt;
    private Date updatedAt;

    private String externalUserId;
    private String externalCoverId;

    private List<PlaylistMusic> playlistMusics;
}
