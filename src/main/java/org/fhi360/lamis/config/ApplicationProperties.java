package org.fhi360.lamis.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Configuration
@Getter
@Setter
public class ApplicationProperties {
    private String contextPath;
    private String uploadDirectory;
    private String databasePath;
    private Security security;
    private final CorsConfiguration cors = new CorsConfiguration();

    @Data
    public static class Security {
        Authentication authentication;

        @Data
        public static class Authentication {
            Jwt jwt;

            @Data
            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe;
            }
        }
    }
}
