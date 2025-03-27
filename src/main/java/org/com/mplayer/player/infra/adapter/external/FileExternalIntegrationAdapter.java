package org.com.mplayer.player.infra.adapter.external;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Component
@AllArgsConstructor
public class FileExternalIntegrationAdapter implements FileExternalIntegrationPort {
    private final S3Client s3Client;

    private final FileUtilsPort fileUtilsPort;

    private final static String s3Region = "us-east-1";

    @Override
    public String insertFile(byte[] fileBytes, String fileName, String destination, String contentType) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(destination)
                .key(fileName)
                .contentType(contentType)
                .contentLength((long) fileBytes.length)
                .build();

            s3Client.putObject(request, RequestBody.fromBytes(fileBytes));

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

            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(destination)
                .key(fileName)
                .contentType(contentType)
                .contentLength(file.length())
                .build();

            s3Client.putObject(request, RequestBody.fromFile(file));

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
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(source)
                .key(fileId)
                .build();

            s3Client.deleteObject(request);
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
