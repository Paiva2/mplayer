package org.com.mplayer.player.infra.adapter.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.mplayer.player.domain.ports.out.external.CoverExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindCoverImageUrlByArtistAndAlbumOutputPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindCoverImageUrlByArtistAndTrackOutputPort;
import org.com.mplayer.player.infra.adapter.data.exception.ExternalErrorException;
import org.com.mplayer.player.infra.constants.ExternalIntegrationUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CoverExternalIntegrationAdapter implements CoverExternalIntegrationPort {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String lastFmUrl = ExternalIntegrationUrl.COVER_IMAGE_SERVICE.SERVICE_1;

    private final RestTemplate restTemplate;

    @Value("${auth.last-fm.api-key}")
    private String lastFmApiKey;

    @Override
    public String findTrackCoverImageUrlByArtistAndTrack(String artist, String track) {
        try {
            String url = lastFmUrl.concat("/?method=track.getInfo&api_key=")
                .concat(lastFmApiKey)
                .concat("&track=")
                .concat(track)
                .concat("&artist=")
                .concat(artist)
                .concat("&format=json");

            ResponseEntity<String> responseFirstService = restTemplate.getForEntity(url, String.class);
            FindCoverImageUrlByArtistAndTrackOutputPort coverBody = objectMapper.readValue(responseFirstService.getBody(), FindCoverImageUrlByArtistAndTrackOutputPort.class);

            Optional<FindCoverImageUrlByArtistAndTrackOutputPort.Track.Album.Image> albumImage = coverBody.getTrack().getAlbum().getImage().stream().filter(albumDto -> albumDto.getSize().equals("extralarge")).findFirst();

            if (albumImage.isPresent()) {
                return albumImage.get().getUrl();
            } else {
                Optional<FindCoverImageUrlByArtistAndTrackOutputPort.Track.Album.Image> alternativeCoverImageSize = coverBody.getTrack().getAlbum().getImage().stream().filter(albumDto -> albumDto.getSize().equals("large") || albumDto.getSize().equals("medium") || albumDto.getSize().equals("mega")).findFirst();

                if (alternativeCoverImageSize.isPresent()) {
                    return alternativeCoverImageSize.get().getUrl();
                } else {
                    return "";
                }
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            String message = "[CoverExternalIntegrationAdapter.findTrackCoverImageUrlByArtistAndTrack] Error: " + e.getMessage();
            log.error(message);

            if (e.getStatusCode().is4xxClientError()) {
                return "";
            }

            throw new ExternalErrorException("Error while fetching cover image...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findAlbumCoverImageUrlByArtist(String artist, String albumName) {
        try {
            String url = lastFmUrl.concat("/?method=album.getInfo&api_key=")
                .concat(lastFmApiKey)
                .concat("&artist=")
                .concat(artist)
                .concat("&album=")
                .concat(albumName)
                .concat("&format=json");

            ResponseEntity<String> responseFirstService = restTemplate.getForEntity(url, String.class);
            FindCoverImageUrlByArtistAndAlbumOutputPort coverBody = objectMapper.readValue(responseFirstService.getBody(), FindCoverImageUrlByArtistAndAlbumOutputPort.class);

            Optional<FindCoverImageUrlByArtistAndAlbumOutputPort.Album.Image> albumImage = coverBody.getAlbum().getImage().stream().filter(albumDto -> albumDto.getSize().equals("extralarge")).findFirst();

            if (albumImage.isPresent()) {
                return albumImage.get().getUrl();
            } else {
                Optional<FindCoverImageUrlByArtistAndAlbumOutputPort.Album.Image> alternativeCoverImageSize = coverBody.getAlbum().getImage().stream().filter(albumDto -> albumDto.getSize().equals("large") || albumDto.getSize().equals("medium") || albumDto.getSize().equals("mega")).findFirst();

                if (alternativeCoverImageSize.isPresent()) {
                    return alternativeCoverImageSize.get().getUrl();
                } else {
                    return "";
                }
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            String message = "[CoverExternalIntegrationAdapter.findAlbumCoverImageUrlByArtist] Error: " + e.getMessage();
            log.error(message);

            if (e.getStatusCode().is4xxClientError()) {
                return "";
            }

            throw new ExternalErrorException("Error while fetching cover image...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
