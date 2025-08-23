package com.vibragram.backend.model;

public enum MediaType {
    IMAGE("image"),
    VIDEO("video");

    private String mediaType;


    MediaType(String mediaType) {
        this.mediaType = mediaType;
    }


    public String getMediaType() {
        return mediaType;
    }
}
