package org.com.mplayer.player.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicQueue {
    private KeyId id;
    private Integer position;
    private Date createdAt;

    private Queue queue;
    private Music music;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private Long queueId;
        private Long musicId;
        private Integer position;
    }
}
