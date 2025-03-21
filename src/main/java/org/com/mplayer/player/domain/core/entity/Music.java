package org.com.mplayer.player.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.enums.EFileType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Music {
    private Long id;
    private String title;
    private String artist;
    private String genre;
    private String releaseYear;
    private String coverUrl;
    private String composer;
    private Long durationSeconds;
    private EFileType fileType;
    private Date createdAt;
    private String repositoryUrl;

    private Lyric lyric;
    private String externalUserId;
    private Collection collection;
    private List<MusicQueue> musicsQueue;
}
