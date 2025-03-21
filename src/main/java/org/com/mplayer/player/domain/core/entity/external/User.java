package org.com.mplayer.player.domain.core.entity.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;
}
