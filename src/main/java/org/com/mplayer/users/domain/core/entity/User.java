package org.com.mplayer.users.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String profilePicture;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;

    private List<UserRole> userRoles;
    private List<Follower> following;
    private List<Follower> followers;
}
