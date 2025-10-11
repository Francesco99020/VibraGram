package com.vibragram.backend.model.Response;

import java.util.UUID;

public class UploadSessionIdResponse {
    private UUID uploadSessionId;


    public UploadSessionIdResponse(UUID uploadSessionId) {
        this.uploadSessionId = uploadSessionId;
    }

    public UUID getUploadSessionId() {
        return uploadSessionId;
    }
}
