package com.vibragram.backend.model;

import java.util.UUID;

public class Media {
    private Long mediaId;
    private UUID uploadSessionId;
    private Long postId;
    private MediaType mediaType;
    private String mediaUrl;
    private int mediaOrder;

    public Media(Long mediaId, UUID uploadSessionId, Long postId, MediaType mediaType, String mediaUrl, int mediaOrder) {
        this.mediaId = mediaId;
        this.uploadSessionId = uploadSessionId;
        this.postId = postId;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.mediaOrder = mediaOrder;
    }

    public Media(){}

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public UUID getUploadSessionId() {
        return uploadSessionId;
    }

    public void setUploadSessionId(UUID uploadSessionId) {
        this.uploadSessionId = uploadSessionId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getMediaOrder() {
        return mediaOrder;
    }

    public void setMediaOrder(int mediaOrder) {
        this.mediaOrder = mediaOrder;
    }
}
