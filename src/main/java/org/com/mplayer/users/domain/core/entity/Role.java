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
public class Role {
    private Long id;
    private String name;
    private Date createdAt;

    private List<UserRole> userRoles;
}
