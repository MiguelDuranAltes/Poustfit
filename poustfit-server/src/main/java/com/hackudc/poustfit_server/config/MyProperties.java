package com.hackudc.poustfit_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "properties")
public class MyProperties {

    private String clientHost;

    private String jwtSecretKey;

    private Long expirationInMinutes;

    private String jwtCookieName;

    private Long maxFileSize;

    private String mediaPathUser;

    private String mediaPathPost;

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public Long getExpirationInMinutes() {
        return expirationInMinutes;
    }

    public void setExpirationInMinutes(Long expirationInMinutes) {
        this.expirationInMinutes = expirationInMinutes;
    }

    public String getJwtCookieName() {
        return jwtCookieName;
    }

    public void setJwtCookieName(String jwtCookieName) {
        this.jwtCookieName = jwtCookieName;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getMediaPathUser() {
        return mediaPathUser;
    }

    public void setMediaPathUser(String mediaPathUser) {
        this.mediaPathUser = mediaPathUser;
    }

    public String getMediaPathPost() {
        return mediaPathPost;
    }

    public void setMediaPathPost(String mediaPathPost) {
        this.mediaPathPost = mediaPathPost;
    }
}
