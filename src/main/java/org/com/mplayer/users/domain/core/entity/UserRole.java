package org.com.mplayer.users.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private KeyId id;
    private User user;
    private Role role;
    private Date createdAt;
    private Date updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long userId;
        private Long roleId;
    }
}
