package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.external.UserRole;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserExternalProfileDTO {
    private Long id;
    private String email;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private List<UserRoleOutput> userRoles;

    @Data
    @NoArgsConstructor
    public static class UserRoleOutput {
        private Long roleId;
        private String role;

        public UserRoleOutput(UserRole userRole) {
            this.roleId = userRole.getRole().getId();
            this.role = userRole.getRole().getName().name();
        }
    }
}
