package com.vibragram.backend.controller;

import com.vibragram.backend.model.BioUpdateRequest;
import com.vibragram.backend.model.FullNameUpdateRequest;
import com.vibragram.backend.model.GenderUpdateRequest;
import com.vibragram.backend.security.AppUserService;
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

    @Autowired
    private final AppUserService appUserService;

    public UserController(UserService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @PostMapping("/profile-photo")
    public ResponseEntity<?> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        try {
            Result<String> result = service.uploadProfilePhoto(userId, file);

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

    @PostMapping("/bio")
    public ResponseEntity<?> uploadBio(
            @RequestBody BioUpdateRequest request,
            Principal principal) {
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<String> result = service.uploadBio(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Bio updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/full-name")
    public ResponseEntity<?> uploadFullName(
            @RequestBody FullNameUpdateRequest request,
            Principal principal
            ){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<String> result = service.uploadFullName(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Full name updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/gender")
    public ResponseEntity<?> uploadGender(
            @RequestBody GenderUpdateRequest request,
            Principal principal
            ){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<GenderUpdateRequest> result = service.uploadGender(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.ok("Gender updated successfully.");
        } else {
            return ErrorResponse.build(result);
        }
    }
}
