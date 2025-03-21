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
public class Lyric {
    private Long id;
    private String lyric;
    private Date createdAt;
    private Date updatedAt;

    private Music music;
}
