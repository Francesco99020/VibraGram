package com.vibragram.backend.controller;

import com.vibragram.backend.model.*;
import com.vibragram.backend.model.Response.UploadSessionIdResponse;
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
            return ResponseEntity.ok(new UploadSessionIdResponse(result.getPayload()));
        } else {
            return ResponseEntity.badRequest().body(result.getMessages());
        }
    }

    //TODO: Issue with response message, needs investigating returns 200 and 500
    //TODO: Endpoint invokes this error:  Could not write JSON: Cannot invoke "java.lang.Long.longValue()" because "this.postId" is null
    @PostMapping("/{uploadSessionId}/upload/{mediaOrder}")
    public ResponseEntity<?> uploadMediaForPost(
            @PathVariable String uploadSessionId,
            @PathVariable int mediaOrder,
            @RequestParam("file")MultipartFile file,
            Principal principal
            ){
        //verify upload session id belongs to user
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();
        UUID uploadSessionUUID = UUID.fromString(uploadSessionId);
        Result<Long> userResult = service.getUserIdOfUploadSession(uploadSessionUUID);
        if(!userResult.isSuccess()){
            return ResponseEntity.badRequest().body(userResult.getMessages());
        }
        if(userResult.getPayload() != userId){
            return ResponseEntity.badRequest().body("Not authorized to use this upload session");
        }
        try{
            Result<Media> result = service.uploadMediaForPost(uploadSessionUUID, file, mediaOrder);
            if(result.isSuccess()){
                return ResponseEntity.ok(result.getPayload());
            } else {
                return ResponseEntity.badRequest().body(result.getMessages());
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Could not upload file.");
    }
}
