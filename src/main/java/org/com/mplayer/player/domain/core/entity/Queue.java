package org.com.mplayer.player.domain.core.entity;

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
public class Queue {
    private Long id;
    private String externalUserId;
    private Date createdAt;
    private Date updatedAt;

    private List<MusicQueue> musicQueue;
}
