package org.com.mplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MplayerApplication {
    public final static String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        SpringApplication.run(MplayerApplication.class, args);
    }
}
