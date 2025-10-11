package com.vibragram.backend.model;

import java.time.LocalDateTime;

public class Post {
    private Long postId;
    private long userId;
    private String caption;
    private String location;
    private LocalDateTime createdAt;

    public Post(Long postId, long userId, String caption, String location, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.caption = caption;
        this.location = location;
        this.createdAt = createdAt;
    }

    public Post(){}

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
