package org.com.mplayer.player.domain.core.entity.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.enums.ERole;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private ERole name;
    private Date createdAt;
}
