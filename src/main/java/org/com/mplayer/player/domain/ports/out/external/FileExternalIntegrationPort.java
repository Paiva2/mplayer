package org.com.mplayer.player.domain.ports.out.external;

import org.springframework.web.multipart.MultipartFile;

public interface FileExternalIntegrationPort {
    String insertFile(byte[] fileBytes, String fileName, String destination, String contentType);

    String insertFile(MultipartFile multipartFile, String fileIdentification, String destination, String contentType);
}
