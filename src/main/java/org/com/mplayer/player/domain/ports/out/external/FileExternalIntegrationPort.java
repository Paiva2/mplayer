package org.com.mplayer.player.domain.ports.out.external;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface FileExternalIntegrationPort {
    String insertFile(byte[] fileBytes, String fileName, String destination, String contentType);

    String insertFile(MultipartFile multipartFile, String fileName, String destination, String contentType);

    void removeFile(String source, String fileId);

    HashMap<String, Object> streamFile(String source, String fileId, String byteRange);
}
