package org.com.mplayer.player.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_playlist", schema = "player")
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_playlist_id_seq")
    @SequenceGenerator(schema = "player", name = "tb_playlist_id_seq", sequenceName = "tb_playlist_id_seq", allocationSize = 1)
    @Column(name = "ply_id")
    private Long id;

    @Column(name = "ply_name")
    private String name;

    @Column(name = "ply_cover_url")
    private String coverUrl;

    @Column(name = "ply_visible_public")
    private Boolean visiblePublic;

    @CreationTimestamp
    @Column(name = "ply_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ply_updated_at")
    private Date updatedAt;

    @Column(name = "ply_external_user_id")
    private String externalUserId;
    
    @Column(name = "ply_external_cover_id")
    private String externalCoverId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playlist")
    private List<PlaylistMusicEntity> playlistMusics;
}
