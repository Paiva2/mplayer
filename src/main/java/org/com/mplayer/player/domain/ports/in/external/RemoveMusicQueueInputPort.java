package org.com.mplayer.player.domain.ports.in.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMusicQueueInputPort {
    Long musicId;
    Integer position;
}
