package com.vibragram.backend.controller;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.service.MediaService;
import com.vibragram.backend.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = {"http://localhost:3000"})
public class MediaController {
    @Autowired
    private final MediaService service;


    public MediaController(MediaService service) {
        this.service = service;
    }

    @GetMapping("/{id}/init")
    public ResponseEntity<?> getNewUploadSessionId(
            @PathVariable long id
    ){
        Result<UUID> result = service.getNewUploadSessionId(id);
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
