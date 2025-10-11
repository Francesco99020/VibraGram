package com.vibragram.backend.model.Response;

import com.vibragram.backend.model.Media;

import java.time.LocalDateTime;
import java.util.List;

public class PostFetchResponse {
    private Long postId;
    private long userId;
    private String caption;
    private String location;
    private LocalDateTime createdAt;
    private List<Media> media;


    public PostFetchResponse(Long postId, long userId, String caption, String location, LocalDateTime createdAt, List<Media> media) {
        this.postId = postId;
        this.userId = userId;
        this.caption = caption;
        this.location = location;
        this.createdAt = createdAt;
        this.media = media;
    }

    public PostFetchResponse(){}

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

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
