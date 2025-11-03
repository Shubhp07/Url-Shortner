package com.example.Url_Shortner.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private long clickCount;

    public UrlStatsResponse(String originalUrl, String shortUrl, LocalDateTime creationDate, long clickCount) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.creationDate = creationDate;
        this.clickCount = clickCount;
    }

    public String getOriginalUrl() { return originalUrl; }
    public String getShortUrl() { return shortUrl; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public long getClickCount() { return clickCount; }
}
