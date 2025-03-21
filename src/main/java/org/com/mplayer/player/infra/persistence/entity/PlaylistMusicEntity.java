package org.com.mplayer.player.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_playlist_music", schema = "player")
public class PlaylistMusicEntity implements Serializable {
    @EmbeddedId
    private KeyId id;

    @Column(name = "plm_position")
    private Integer position;

    @CreationTimestamp
    @Column(name = "plm_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "plm_updated_at")
    private Date updatedAt;

    @MapsId("playlistId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plm_playlist_id")
    private PlaylistEntity playlist;

    @MapsId("musicId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plm_music_id")
    private MusicEntity music;

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long playlistId;
        private Long musicId;
    }
}
