package org.com.mplayer.player.domain.ports.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.mplayer.player.domain.core.entity.Collection;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionOutput {
    private Long id;
    private String title;
    private String artist;
    private String imageUrl;
    private Integer musicQuantity;
    private Date createdAt;

    public CollectionOutput(Collection collection) {
        this.id = collection.getId();
        this.title = collection.getTitle();
        this.artist = collection.getArtist();
        this.imageUrl = collection.getImageUrl();
        this.musicQuantity = collection.getMusics() != null ? collection.getMusics().size() : 0;
        this.createdAt = collection.getCreatedAt();
    }
}
