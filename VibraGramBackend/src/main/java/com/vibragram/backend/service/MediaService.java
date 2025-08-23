package com.vibragram.backend.service;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.model.UploadSessionStatus;
import com.vibragram.backend.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {
    @Autowired
    private final MediaRepository repository;

    private final Path uploadDir = Paths.get("uploads/media");

    public MediaService(MediaRepository repository) throws IOException {
        this.repository = repository;
        // Ensure upload directory exists
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public Result<UUID> getNewUploadSessionId(long id){
        Result<UUID> result = new Result<>();

        if(isValidSession(id)){
            result.addMessage("User already has active upload session.", ResultType.INVALID);
            return result;
        }

        UploadSession uploadSession = new UploadSession();
        uploadSession.setUploadSessionId(UUID.randomUUID());
        uploadSession.setCreatedAt(LocalDateTime.now());
        uploadSession.setExpiresAt(LocalDateTime.now().plusHours(1));
        uploadSession.setStatus(UploadSessionStatus.ACTIVE);
        uploadSession.setUserId(id);

        if(repository.createUploadSession(uploadSession)){
            result.setPayload(uploadSession.getUploadSessionId());
        } else {
            result.addMessage("Could not create new upload_session", ResultType.INVALID);
        }

        return result;
    }

//    public Result<Media> uploadMediaForPost(UUID uploadSessionId, MultipartFile file) {
//        Result<Media> result = new Result<>();
//
//        try {
//            // 1. Validate upload session
//            if (!isValidSession(uploadSessionId)) {
//                result.addMessage("Invalid or expired upload session.", ResultType.INVALID);
//                return result;
//            }
//
//            // 2. Validate file presence
//            if (file == null || file.isEmpty()) {
//                result.addMessage("No file provided.", ResultType.INVALID);
//                return result;
//            }
//
//            // 3. Validate file type
//            String originalName = file.getOriginalFilename();
//            String extension = getFileExtension(originalName);
//            if (!isAllowedExtension(extension)) {
//                result.addMessage("Unsupported file type: " + extension, ResultType.INVALID);
//                return result;
//            }
//
//            // 4. Validate file size (example: max 50 MB)
//            long maxSize = 50L * 1024 * 1024;
//            if (file.getSize() > maxSize) {
//                result.addMessage("File exceeds max size of 50MB.", ResultType.INVALID);
//                return result;
//            }
//
//            // 5. Generate unique filename
//            String uniqueName = UUID.randomUUID().toString() + "." + extension;
//
//            // 6. Save file to disk
//            Path destination = uploadDir.resolve(uniqueName);
//            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//
//            // 7. Save metadata to database
//            Media media = new Media();
//            media.setUploadSessionId(uploadSessionId);
//
//
//            media.setOriginalFilename(originalName);
//            media.setStoredFilename(uniqueName);
//            media.setFileType(extension);
//            media.setSize(file.getSize());
//            media.setCreatedAt(Instant.now());
//
//            Media saved = mediaRepository.save(media);
//
//            result.setPayload(saved);
//
//        } catch (IOException e) {
//            result.addMessage("Error saving file: " + e.getMessage(), ResultType.INVALID);
//        } catch (Exception e) {
//            result.addMessage("Unexpected error: " + e.getMessage(), ResultType.INVALID);
//        }
//
//        return result;
//    }

    // --- Helpers ---

    private boolean isValidSession(UUID sessionId) {
        return repository.getUploadSessionByUUID(sessionId) != null;
    }

    private boolean isValidSession(long userId){
        return repository.getUploadSessionByUserId(userId) != null;
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String ext) {
        List<String> allowed = List.of("png", "jpg", "jpeg", "gif", "mp4", "mov", "avi");
        return allowed.contains(ext);
    }
}
