package org.com.mplayer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    @Value("${auth.aws.access-key}")
    private String accessKey;

    @Value("${auth.aws.secret-key}")
    private String secretKey;

    public final static Region AWS_S3_REGION = Region.US_EAST_1;

    @Bean
    public S3Client S3Client() {
        return S3Client.builder()
            .region(AWS_S3_REGION)
            .credentialsProvider(() -> new AwsCredentials() {
                @Override
                public String accessKeyId() {
                    return accessKey;
                }

                @Override
                public String secretAccessKey() {
                    return secretKey;
                }
            }).build();
    }
}
