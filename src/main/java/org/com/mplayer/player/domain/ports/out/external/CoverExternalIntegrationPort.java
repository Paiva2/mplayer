package org.com.mplayer.player.domain.ports.out.external;

public interface CoverExternalIntegrationPort {
    String findTrackCoverImageUrlByArtistAndTrack(String artist, String track);

    String findAlbumCoverImageUrlByArtist(String artist, String albumName);
}
