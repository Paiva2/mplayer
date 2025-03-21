package org.com.mplayer.player.domain.ports.out.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface FileUtilsPort {
    Map<String, Object> readFileMetadata(MultipartFile multipartFile);

    File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException;

    String getContentType(MultipartFile multipartFile);

    String fileNameWithoutExtension(MultipartFile multipartFile);

    String fileNameWithoutExtension(String fileName);

    String fileExtension(MultipartFile multipartFile);

    String fileExtension(String fileName);
}
