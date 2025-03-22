package org.com.mplayer.player.domain.ports.out.external;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileExternalIntegrationPort {
    Map<String, String> insertFile(byte[] fileBytes, String fileName, String destination, String contentType);

    Map<String, String> insertFile(MultipartFile multipartFile, String fileIdentification, String destination, String contentType);
}
