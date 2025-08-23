package com.vibragram.backend.repository;

import com.vibragram.backend.model.UploadSession;

import java.util.UUID;

public interface MediaRepository {
    public boolean createUploadSession(UploadSession uploadSession);

    public UploadSession getUploadSessionByUUID(UUID uploadSessionId);

    public UploadSession getUploadSessionByUserId(long userId);
}
