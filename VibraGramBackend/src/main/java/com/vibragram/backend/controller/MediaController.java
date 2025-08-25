package com.vibragram.backend.controller;

import com.vibragram.backend.model.AppUser;
import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.model.UploadSessionStatus;
import com.vibragram.backend.security.AppUserService;
import com.vibragram.backend.service.MediaService;
import com.vibragram.backend.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = {"http://localhost:3000"})
public class MediaController {
    @Autowired
    private final MediaService service;
    @Autowired
    private final AppUserService appUserService;


    public MediaController(MediaService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/init")
    public ResponseEntity<?> getNewUploadSessionId(Principal principal){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<UUID> result = service.getNewUploadSessionId(userId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ResponseEntity.badRequest().body(result.getMessages());
        }
    }

    @PutMapping("/{uploadSessionId}/update_status/{status}")
    public ResponseEntity<?> updateUploadSessionStatus(
            @PathVariable UUID uploadSessionId,
            @PathVariable String status
    ){
        Result<UploadSession> result = service.updateUploadSessionStatus(uploadSessionId, UploadSessionStatus.getStatusFromString(status));

        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ResponseEntity.badRequest().body(result.getMessages());
        }
    }

//    @PostMapping("/media/{upload_session_id}/upload")
//    public ResponseEntity<?> uploadMediaForPost(
//            @PathVariable UUID uploadSessionId,
//            @RequestParam("file")MultipartFile file
//            ){
//        try{
//            Result<Media> result = service.uploadMediaForPost(uploadSessionId, file);
//
//            if(result.isSuccess()){
//                return ResponseEntity.ok(result.getPayload());
//            } else {
//                return ResponseEntity.badRequest().body(result.getMessages());
//            }
//        }catch (Exception e){
//            System.err.println(e.getMessage());
//        }
//        return ResponseEntity.badRequest().body("Could not upload file.");
//    }
}
