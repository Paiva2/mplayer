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

@Component
@AllArgsConstructor
public class FileExternalIntegrationAdapter implements FileExternalIntegrationPort {
    private final AmazonS3 s3Client;

    private final FileUtilsPort fileUtilsPort;

    private final static String s3Region = "us-east-1";

    @Override
    public String insertFile(byte[] fileBytes, String fileName, String destination, String contentType) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
            bais.close();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileBytes.length);
            objectMetadata.setContentType(contentType);

            s3Client.putObject(destination, fileName, bais, objectMetadata);

            return mountUrl(destination, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String insertFile(MultipartFile multipartFile, String fileName, String destination, String contentType) {
        File file = null;

        try {
            file = fileUtilsPort.convertMultipartFileToFile(multipartFile);

            s3Client.putObject(destination, fileName, file);

            return mountUrl(destination, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void removeFile(String source, String fileId) {
        try {
            s3Client.deleteObject(source, fileId);
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
