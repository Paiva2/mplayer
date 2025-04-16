package org.com.mplayer.player.domain.ports.out.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindLyricByArtistAndTrackXmlFormatOutputPort {
    @JsonProperty("LyricSong")
    private String lyricSong;

    @JsonProperty("LyricArtist")
    private String LyricArtist;

    @JsonProperty("Lyric")
    private String Lyric;
}
