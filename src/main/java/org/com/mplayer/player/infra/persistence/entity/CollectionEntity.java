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
@Table(name = "tb_collection", schema = "player")
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_collection_id_seq")
    @SequenceGenerator(schema = "player", name = "tb_collection_id_seq", sequenceName = "tb_collection_id_seq", allocationSize = 1)
    @Column(name = "col_id")
    private Long id;

    @Column(name = "col_title")
    private String title;

    @Column(name = "col_artist")
    private String artist;

    @Column(name = "col_image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "col_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "col_updated_at")
    private Date updatedAt;

    @Column(name = "col_external_user_id")
    private String externalUserId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collection")
    private List<MusicEntity> musics;
}
