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
@Table(name = "tb_queue", schema = "player")
public class QueueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_queue_id_seq")
    @SequenceGenerator(schema = "player", name = "tb_queue_id_seq", sequenceName = "tb_queue_id_seq", allocationSize = 1)
    @Column(name = "que_id")
    private Long id;

    @Column(name = "que_external_user_id")
    private String externalUserId;

    @CreationTimestamp
    @Column(name = "que_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "que_updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MusicQueueEntity> musicsQueue;
}
