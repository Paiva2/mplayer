package org.com.mplayer.player.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.mplayer.player.domain.core.enums.EFileType;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_music", schema = "player")
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_music_id_seq")
    @SequenceGenerator(schema = "player", name = "tb_music_id_seq", sequenceName = "tb_music_id_seq", allocationSize = 1)
    @Column(name = "mus_id")
    private Long id;

    @Column(name = "mus_title")
    private String title;

    @Column(name = "mus_artist")
    private String artist;

    @Column(name = "mus_genre")
    private String genre;

    @Column(name = "mus_release_year")
    private String releaseYear;

    @Column(name = "mus_cover_url")
    private String coverUrl;

    @Column(name = "mus_composer")
    private String composer;

    @Column(name = "mus_duration_seconds")
    private Long durationSeconds;

    @Column(name = "mus_file_type")
    @Enumerated(EnumType.STRING)
    private EFileType fileType;

    @CreationTimestamp
    @Column(name = "mus_created_at")
    private Date createdAt;

    @Column(name = "mus_repository_url")
    private String repositoryUrl;

    @Column(name = "mus_external_user_id")
    private String externalUserId;

    @Column(name = "mus_external_id")
    private String externalIdentification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mus_collection_id")
    private CollectionEntity collection;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "music")
    private LyricEntity lyric;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "music")
    private List<MusicQueueEntity> musicsQueue;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "music")
    private List<PlaylistMusicEntity> playlistMusics;
}
