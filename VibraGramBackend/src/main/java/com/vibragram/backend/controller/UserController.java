package com.vibragram.backend.controller;

import com.vibragram.backend.model.Request.BioUpdateRequest;
import com.vibragram.backend.model.Request.FullNameUpdateRequest;
import com.vibragram.backend.model.Request.GenderUpdateRequest;
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

    @PutMapping("/bio")
    public ResponseEntity<?> updateBio(
            @RequestBody BioUpdateRequest request,
            Principal principal) {
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<String> result = service.updateBio(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.status(201).build();
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PutMapping("/full-name")
    public ResponseEntity<?> updateFullName(
            @RequestBody FullNameUpdateRequest request,
            Principal principal
            ){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<String> result = service.updateFullName(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.status(201).build();
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PutMapping("/gender")
    public ResponseEntity<?> updateGender(
            @RequestBody GenderUpdateRequest request,
            Principal principal
            ){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<GenderUpdateRequest> result = service.updateGender(userId, request);
        if(result.isSuccess()){
            return ResponseEntity.status(201).build();
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PutMapping("/is-public/{isPublic}")
    public ResponseEntity<?> updateIsPublic(
            Principal principal,
            @PathVariable boolean isPublic
    ){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<Boolean> result = service.updateIsPublic(userId, isPublic);
        if(result.isSuccess()){
            return ResponseEntity.status(201).build();
        } else {
            return ErrorResponse.build(result);
        }
    }
}
