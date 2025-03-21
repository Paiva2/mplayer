package org.com.mplayer.player.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_lyric", schema = "player")
public class LyricEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_lyric_id_seq")
    @SequenceGenerator(schema = "player", name = "tb_lyric_id_seq", sequenceName = "tb_lyric_id_seq", allocationSize = 1)
    @Column(name = "lyr_id")
    private Long id;

    @Column(name = "lyr_lyric")
    private String lyric;

    @CreationTimestamp
    @Column(name = "lyr_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "lyr_updated_at")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lyr_music_id")
    private MusicEntity music;
}
