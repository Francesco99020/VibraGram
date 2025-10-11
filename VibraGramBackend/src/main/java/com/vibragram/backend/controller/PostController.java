package com.vibragram.backend.controller;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.Post;
import com.vibragram.backend.model.Request.PostRequest;
import com.vibragram.backend.model.Response.PostFetchResponse;
import com.vibragram.backend.security.AppUserService;
import com.vibragram.backend.service.MediaService;
import com.vibragram.backend.service.PostService;
import com.vibragram.backend.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = {"http://localhost:3000"})
public class PostController {
    @Autowired
    private final PostService service;

    @Autowired
    private final MediaService mediaService;

    @Autowired
    private final AppUserService appUserService;

    public PostController(PostService service, MediaService mediaService, AppUserService appUserService) {
        this.service = service;
        this.mediaService = mediaService;
        this.appUserService = appUserService;
    }

    @PostMapping("/{uploadSessionId}/create")
    public ResponseEntity<?> createNewPost(
            @PathVariable String uploadSessionId,
            @RequestBody PostRequest request,
            Principal principal
            ){
        //verify upload session id belongs to user
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();
        UUID uploadSessionUUID = UUID.fromString(uploadSessionId);
        Result<Long> userResult = mediaService.getUserIdOfUploadSession(uploadSessionUUID);
        if(!userResult.isSuccess()){
            return ResponseEntity.badRequest().body(userResult.getMessages());
        }
        if(userResult.getPayload() != userId){
            return ResponseEntity.badRequest().body("Not authorized to use this upload session");
        }

        //pass request, uploadSessionId and userId down to service to create post
        Result<Post> result = service.createPost(request, uploadSessionUUID, userId);

        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(
            @PathVariable long postId
    ){
        //get post metadata
        Result<Post> postResult = service.getPostById(postId);

        if(!postResult.isSuccess()){
            return ErrorResponse.build(postResult);
        }

        Post post = postResult.getPayload();

        //collect all media for this post
        List<Media> media = mediaService.getMediaLinkedToPost(postId);

        if(media == null){
            ErrorResponse.build("No media found for this post");
        }

        //pack all data into a response object
        PostFetchResponse postFetchResponse = new PostFetchResponse(
                post.getPostId(),
                post.getUserId(),
                post.getCaption(),
                post.getLocation(),
                post.getCreatedAt(),
                media
        );

        return ResponseEntity.ok(postFetchResponse);
    }
}
