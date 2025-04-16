package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamMusicOutputPort {
    private byte[] content;
    private long rangeLength;
    private long start;
    private long end;
    private long fileSize;
    private String mediaType;
}
