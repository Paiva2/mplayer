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
public class Follower {
    private Long id;
    private User user;
    private User userFollowed;
    private Date createdAt;
}
