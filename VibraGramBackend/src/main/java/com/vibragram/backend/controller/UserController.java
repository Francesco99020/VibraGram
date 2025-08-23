package com.vibragram.backend.controller;

import com.vibragram.backend.model.BioUpdateRequest;
import com.vibragram.backend.model.FullNameUpdateRequest;
import com.vibragram.backend.model.GenderUpdateRequest;
import com.vibragram.backend.service.Result;
import com.vibragram.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {
    @Autowired
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/{id}/profile-photo")
    public ResponseEntity<?> uploadProfilePhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Principal principal) {

        try {
            Result<String> result = service.uploadProfilePhoto(id, file);

            if (result.isSuccess()) {
                return ResponseEntity.ok("Profile photo uploaded successfully.");
            } else {
                return ErrorResponse.build(result);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload file.");
        }
    }

    @PostMapping("/{id}/bio")
    public ResponseEntity<?> uploadBio(
            @PathVariable Long id,
            @RequestBody BioUpdateRequest request) {
        Result<String> result = service.uploadBio(id, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Bio updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/{id}/full-name")
    public ResponseEntity<?> uploadFullName(
            @PathVariable Long id,
            @RequestBody FullNameUpdateRequest request
            ){
        Result<String> result = service.uploadFullName(id, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Full name updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/{id}/gender")
    public ResponseEntity<?> uploadGender(
            @PathVariable Long id,
            @RequestBody GenderUpdateRequest request
            ){
        Result<GenderUpdateRequest> result = service.uploadGender(id, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Gender updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }
}
