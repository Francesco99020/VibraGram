package com.vibragram.backend.repository;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.model.UploadSessionStatus;

import java.util.List;
import java.util.UUID;

public interface MediaRepository {
    public boolean createUploadSession(UploadSession uploadSession);

    public UploadSession getUploadSessionByUUID(UUID uploadSessionId);

    public UploadSession getUploadSessionByUserId(long userId);

    public boolean setUploadSessionStatus(UUID uploadSessionId, UploadSessionStatus status);

    public Long getUserIdOfUploadSession(UUID uploadSessionId);

    public boolean createMedia(Media media);

    public List<Media> getMediaByUploadSession(UUID uploadSessionId);

    public List<Media> getMediaByPostId(long postId);

    public boolean updateMedia(Media media);
}
