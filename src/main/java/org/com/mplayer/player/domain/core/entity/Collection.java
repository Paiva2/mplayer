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
public class Collection {
    private Long id;
    private String title;
    private String artist;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;

    private List<Music> musics;
    private String externalUserId;
}
