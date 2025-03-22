package org.com.mplayer.player.infra.adapter.external;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FileExternalIntegrationAdapter implements FileExternalIntegrationPort {
    private final AmazonS3 s3Client;

    private final FileUtilsPort fileUtilsPort;

    private final static String s3Region = "us-east-1";

    @Override
    public Map<String, String> insertFile(byte[] fileBytes, String fileName, String destination, String contentType) {
        try {
            String newName = fileName.concat("_").concat(UUID.randomUUID().toString()).concat(".").concat(contentType);

            ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
            bais.close();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileBytes.length);
            objectMetadata.setContentType(contentType);

            s3Client.putObject(destination, newName, bais, objectMetadata);

            String url = mountUrl(destination, newName);

            return new HashMap<>() {{
                put("url", url);
                put("id", newName);
            }};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> insertFile(MultipartFile multipartFile, String fileIdentification, String destination, String contentType) {
        File file = null;

        try {
            String originalName = fileUtilsPort.fileNameWithoutExtension(multipartFile);
            String newName = fileIdentification.concat("_").concat(originalName.concat("_").concat(UUID.randomUUID().toString()).concat(".").concat(fileUtilsPort.fileExtension(multipartFile)));

            file = fileUtilsPort.convertMultipartFileToFile(multipartFile);

            s3Client.putObject(destination, newName, file);
            String url = mountUrl(destination, newName);

            return new HashMap<>() {{
                put("url", url);
                put("id", newName);
            }};
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    private String mountUrl(String destination, String filename) {
        StringBuilder builder = new StringBuilder();
        builder.append("https://")
            .append(destination)
            .append(".s3.")
            .append(s3Region)
            .append(".amazonaws.com/")
            .append(filename);

        return builder.toString();
    }
}
