package com.vibragram.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UploadSession {
    private UUID uploadSessionId;
    private long userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private long postId;

    public UploadSession(UUID uploadSessionId, long userId, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.uploadSessionId = uploadSessionId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UploadSession(){}

    public UUID getUploadSessionId() {
        return uploadSessionId;
    }

    public void setUploadSessionId(UUID uploadSessionId) {
        this.uploadSessionId = uploadSessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
