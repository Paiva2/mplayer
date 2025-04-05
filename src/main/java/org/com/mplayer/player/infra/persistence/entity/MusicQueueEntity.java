package org.com.mplayer.player.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_music_queue", schema = "player")
public class MusicQueueEntity {
    @EmbeddedId
    private KeyId id;

    @Column(name = "muq_position")
    private Integer position;

    @CreationTimestamp
    @Column(name = "muq_created_at")
    private Date createdAt;

    @MapsId("queueId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "muq_queue_id")
    private QueueEntity queue;

    @MapsId("musicId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "muq_music_id")
    private MusicEntity music;

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long musicId;
        private Long queueId;
        @Column(name = "muq_position", insertable = false, updatable = false)
        private Integer position;
    }
}
