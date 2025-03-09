package org.com.mplayer.users.infra.persistence.entity;

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
@Table(name = "tb_user_role")
public class UserRoleEntity implements Serializable {
    @EmbeddedId
    private KeyId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "url_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "url_role_id")
    private RoleEntity role;

    @CreationTimestamp
    @Column(name = "url_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "url_updated_at")
    private Date updatedAt;

    @Getter
    @Setter
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long userId;
        private Long roleId;
    }
}
