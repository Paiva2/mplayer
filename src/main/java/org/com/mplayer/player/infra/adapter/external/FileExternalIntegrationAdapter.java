package org.com.mplayer.player.infra.adapter.external;

import lombok.AllArgsConstructor;
import org.com.mplayer.player.domain.ports.out.external.FileExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

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

    @Override
    public HashMap<String, Object> streamFile(String source, String fileId, String byteRange) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
            .bucket(source)
            .key(fileId)
            .build();

        HeadObjectResponse objectMetadata = s3Client.headObject(headObjectRequest);
        long fileSize = objectMetadata.contentLength();

        long[] range = parseRange(byteRange, fileSize);
        long start = range[0];
        long end = range[1];
        long rangeLength = end - start + 1;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(source)
            .key(fileId)
            .range("bytes=" + start + "-" + end)
            .build();

        byte[] content = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();

        return new HashMap<>() {{
            put("content", content);
            put("rangeLength", rangeLength);
            put("start", start);
            put("end", end);
            put("fileSize", fileSize);
        }};
    }

    private long[] parseRange(String rangeHeader, long fileSize) {
        BigDecimal defaultEndValuePercentage = new BigDecimal("0.05"); // 5%
        BigDecimal minEndValue = defaultEndValuePercentage.multiply(new BigDecimal(fileSize - 1));
        long defaultMinEndValue = Math.min(minEndValue.longValue(), fileSize - 1);

        if (rangeHeader == null || !rangeHeader.contains("bytes=")) {
            return new long[]{0, defaultMinEndValue};
        }

        try {
            String byteRange = rangeHeader.replace("bytes=", "");
            String[] ranges = byteRange.split("-");

            if (ranges.length < 1) {
                return new long[]{0, defaultMinEndValue};
            }

            long start = Math.max(0, Long.parseLong(ranges[1].replaceAll("\\.", "")));
            long endParsed = start + 2048L;
            long end = Math.min(endParsed, fileSize - 1);

            return new long[]{start, end};
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing range: " + rangeHeader, e);
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
