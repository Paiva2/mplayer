package org.com.mplayer.users.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.mplayer.users.domain.core.enums.ERole;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_role_id_seq")
    @SequenceGenerator(name = "tb_role_id_seq", sequenceName = "tb_role_id_seq", allocationSize = 1)
    @Column(name = "rl_id")
    private Long id;

    @Column(name = "rl_name")
    @Enumerated(EnumType.STRING)
    private ERole name;

    @CreationTimestamp
    @Column(name = "rl_created_at")
    private Date createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<UserRoleEntity> userRoles;
}
