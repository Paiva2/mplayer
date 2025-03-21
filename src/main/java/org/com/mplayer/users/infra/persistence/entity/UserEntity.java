package org.com.mplayer.users.infra.persistence.entity;

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
@Table(name = "tb_user", schema = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_user_id_seq")
    @SequenceGenerator(schema = "users", name = "tb_user_id_seq", sequenceName = "tb_user_id_seq", allocationSize = 1)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_first_name")
    private String firstName;

    @Column(name = "usr_last_name")
    private String lastName;

    @Column(name = "usr_password")
    private String password;

    @Column(name = "usr_email")
    private String email;

    @Column(name = "usr_profile_picture")
    private String profilePicture;

    @Column(name = "usr_enabled")
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "usr_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserRoleEntity> userRoles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<FollowerEntity> following;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userFollowed")
    private List<FollowerEntity> followers;
}
