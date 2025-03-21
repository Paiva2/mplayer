package org.com.mplayer.player.infra.adapter.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.mplayer.player.domain.ports.out.external.LyricsExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindLyricByArtistAndTrackDTO;
import org.com.mplayer.player.domain.ports.out.external.dto.FindLyricByArtistAndTrackXmlFormatDTO;
import org.com.mplayer.player.infra.adapter.data.exception.ExternalErrorException;
import org.com.mplayer.player.infra.constants.ExternalIntegrationUrl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@AllArgsConstructor
public class LyricsExternalIntegrationAdapter implements LyricsExternalIntegrationPort {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static ObjectMapper xmlMapper = new XmlMapper();

    private final RestTemplate restTemplate;

    @Override
    public String findLyricByArtistAndTrack(String artist, String track) {
        try {
            String urlFirstService = ExternalIntegrationUrl.LYRICS_SERVICE.SERVICE_1
                .concat("/")
                .concat(artist)
                .concat("/")
                .concat(track);

            ResponseEntity<String> responseFirstService = restTemplate.getForEntity(urlFirstService, String.class);
            FindLyricByArtistAndTrackDTO lyricBody = objectMapper.readValue(responseFirstService.getBody(), FindLyricByArtistAndTrackDTO.class);

            return lyricBody.getLyrics();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            String message = "[LyricsExternalIntegrationAdapter.findLyricByArtistAndTrack] Error: " + e.getMessage();
            log.error(message);

            if (e.getStatusCode().is4xxClientError()) {
                return getLyricFromAlternativeSource(artist, track);
            } else {
                throw new ExternalErrorException("Error while fetching music lyric...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getLyricFromAlternativeSource(String artist, String track) {
        try {
            String urlSecondService = ExternalIntegrationUrl.LYRICS_SERVICE.SERVICE_2
                .concat("/SearchLyricDirect?artist=")
                .concat(artist)
                .concat("&song=")
                .concat(track);

            ResponseEntity<String> responseSecondService = restTemplate.getForEntity(urlSecondService, String.class);
            FindLyricByArtistAndTrackXmlFormatDTO lyric = xmlMapper.readValue(responseSecondService.getBody(), FindLyricByArtistAndTrackXmlFormatDTO.class);

            return lyric.getLyric();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            String message = "[LyricsExternalIntegrationAdapter.getLyricFromAlternativeSource] Error: " + e.getMessage();
            log.error(message);

            throw new ExternalErrorException("Error while fetching music lyric from alternative source...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
