package org.com.mplayer.player.infra.adapter.external;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FileExternalIntegrationAdapter implements FileExternalIntegrationPort {
    private final AmazonS3 s3Client;

    private final FileUtilsPort fileUtilsPort;

    private final static String s3Region = "us-east-1";

    @Override
    public String insertFile(byte[] fileBytes, String fileName, String destination, String contentType) {
        try {
            String newName = fileName.concat("_").concat(UUID.randomUUID().toString()).concat(".").concat(contentType);

            ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
            bais.close();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileBytes.length);
            objectMetadata.setContentType(contentType);

            s3Client.putObject(destination, newName, bais, objectMetadata);

            return mountUrl(destination, newName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String insertFile(MultipartFile multipartFile, String fileIdentification, String destination, String contentType) {
        try {
            String originalName = fileUtilsPort.fileNameWithoutExtension(multipartFile);
            String newName = fileIdentification.concat("_").concat(originalName.concat("_").concat(UUID.randomUUID().toString()).concat(".").concat(fileUtilsPort.fileExtension(multipartFile)));

            s3Client.putObject(destination, newName, fileUtilsPort.convertMultipartFileToFile(multipartFile));
            return mountUrl(destination, newName);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
