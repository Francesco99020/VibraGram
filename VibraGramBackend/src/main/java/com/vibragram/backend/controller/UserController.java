package com.vibragram.backend.controller;

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
                return ResponseEntity.badRequest().body(result.getMessages());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload file.");
        }
    }


}
