package org.com.mplayer.users.infra.persistence.entity;

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
@Table(name = "tb_follower", schema = "users")
public class FollowerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_follower_id_seq")
    @SequenceGenerator(schema = "users", name = "tb_follower_id_seq", sequenceName = "tb_follower_id_seq", allocationSize = 1)
    @Column(name = "flw_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flw_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flw_user_followed_id")
    private UserEntity userFollowed;

    @CreationTimestamp
    @Column(name = "flw_created_at")
    private Date createdAt;
}
