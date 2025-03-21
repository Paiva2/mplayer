package org.com.mplayer.player.domain.ports.out.external;

public interface LyricsExternalIntegrationPort {
    String findLyricByArtistAndTrack(String artist, String track);
}
