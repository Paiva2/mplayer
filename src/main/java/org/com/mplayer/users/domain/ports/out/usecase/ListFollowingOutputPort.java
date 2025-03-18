package org.com.mplayer.users.domain.ports.out.usecase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.users.domain.core.entity.Follower;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListFollowingOutputPort {
    private Long id;
    private UserOutput userFollowed;
    private Date createdAt;

    public ListFollowingOutputPort(Follower follower) {
        this.id = follower.getId();
        this.userFollowed = UserOutput.builder()
            .id(follower.getUser().getId())
            .firstName(follower.getUser().getFirstName())
            .lastName(follower.getUser().getLastName())
            .email(follower.getUser().getEmail())
            .profilePicture(follower.getUser().getProfilePicture())
            .build();
        this.createdAt = follower.getCreatedAt();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserOutput {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String profilePicture;
    }
}
