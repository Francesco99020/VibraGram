package com.vibragram.backend.service;

import com.vibragram.backend.model.BioUpdateRequest;
import com.vibragram.backend.model.FullNameUpdateRequest;
import com.vibragram.backend.model.GenderUpdateRequest;
import com.vibragram.backend.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "gif");
    private final Validator validator;

    @Autowired
    private UserRepository userRepository;

    public UserService(Validator validator) {
        this.validator = validator;
    }

    public Result<String> uploadProfilePhoto(long id, MultipartFile file) throws IOException {
        Result<String> result = new Result<>();

        // Validate file type by MIME
        if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            result.addMessage("Invalid file type. Only images are allowed.", ResultType.INVALID);
            return result;
        }

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            result.addMessage("File is too large. Max size is 5MB.", ResultType.INVALID);
            return result;
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            result.addMessage("Invalid file extension. Allowed: jpg, jpeg, png, gif.", ResultType.INVALID);
            return result;
        }

        // Generate unique file name
        String fileName = UUID.randomUUID() + "-" + originalFilename;
        Path uploadPath = Paths.get("uploads/profile-photos");
        Files.createDirectories(uploadPath);

        //Check if user already has a profile photo
        String existingProfilePic = userRepository.findProfilePhotoUrl(id);
        if(existingProfilePic != null && !existingProfilePic.isBlank()){
            //remove current profile pic from storage
            Path oldFilePath = Paths.get(existingProfilePic);

            if(oldFilePath.startsWith(uploadPath)){
                try{
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e){
                    System.err.println("Failed to delete old profile photo: " + e.getMessage());
                }
            }
        }

        // Save file to disk
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);

        // Save file metadata (URL) in DB
        boolean updated = userRepository.updateProfilePhoto(id, "uploads/profile-photos/" + fileName);

        if (updated) {
            result.setPayload("/uploads/profile-photos/" + fileName);
        } else {
            result.addMessage("Profile photo metadata could not be uploaded.", ResultType.INVALID);
        }

        return result;
    }

    public Result<String> uploadBio(long id, BioUpdateRequest bio){
        Result<String> result = new Result<>();
        Set<ConstraintViolation<BioUpdateRequest>> violations = validator.validate(bio);
        if(!violations.isEmpty()){
            for (ConstraintViolation<BioUpdateRequest> violation : violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }
        if(userRepository.updateBio(id, bio.getBio())){
            result.setPayload(bio.getBio());
        } else {
            result.addMessage("Bio could not be updated.", ResultType.INVALID);
        }
        return result;
    }

    public Result<String> uploadFullName(long id, FullNameUpdateRequest request){
        Result<String> result = new Result<>();
        Set<ConstraintViolation<FullNameUpdateRequest>> violations = validator.validate(request);
        if(!violations.isEmpty()){
            for (ConstraintViolation<FullNameUpdateRequest> violation : violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }
        if(userRepository.updateFullName(id, request.getName())){
            result.setPayload(request.getName());
        } else {
            result.addMessage("Full name could not be updated", ResultType.INVALID);
        }
        return result;
    }

    public Result<GenderUpdateRequest> uploadGender(long id, GenderUpdateRequest request){
        Result<GenderUpdateRequest> result = new Result<>();
        Set<ConstraintViolation<GenderUpdateRequest>> violations = validator.validate(request);
        if(!violations.isEmpty()){
            for (ConstraintViolation<GenderUpdateRequest> violation : violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }
        if(userRepository.updateGender(id, request.getGender())){
            result.setPayload(request);
        } else {
            result.addMessage("Gender could not be updated", ResultType.INVALID);
        }
        return result;
    }
}