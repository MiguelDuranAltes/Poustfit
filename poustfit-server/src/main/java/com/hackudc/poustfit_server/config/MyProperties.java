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

    private String inditexApiUrl;

    private String authUrl;

    private String inditextClient;

    private String inditexPassword;

    private String imgurUrl;

    private String imgurClientId;

    private String imgbbKey;

    private String imgbbUrl;

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

    public String getInditexApiUrl() {
        return inditexApiUrl;
    }

    public void setInditexApiUrl(String inditexApiUrl) {
        this.inditexApiUrl = inditexApiUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getInditextClient() {
        return inditextClient;
    }

    public void setInditexClient(String inditextClient) {
        this.inditextClient = inditextClient;
    }

    public String getInditexPassword() {
        return inditexPassword;
    }

    public void setInditexPassword(String inditexPassword) {
        this.inditexPassword = inditexPassword;
    }

    public String getImgurUrl() {
        return imgurUrl;
    }

    public void setImgurUrl(String imgurUrl) {
        this.imgurUrl = imgurUrl;
    }

    public String getImgurClientId() {
        return imgurClientId;
    }

    public void setImgurClientId(String imgurClientId) {
        this.imgurClientId = imgurClientId;
    }

    public String getImgbbKey() {
        return imgbbKey;
    }

    public void setImgbbKey(String imgbbKey) {
        this.imgbbKey = imgbbKey;
    }

    public String getImgbbUrl() {
        return imgbbUrl;
    }

    public void setImgbbUrl(String imgbbUrl) {
        this.imgbbUrl = imgbbUrl;
    }
}
