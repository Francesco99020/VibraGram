package com.vibragram.backend.controller;

import com.vibragram.backend.security.AppUserService;
import com.vibragram.backend.service.FollowersService;
import com.vibragram.backend.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/followers")
@CrossOrigin(origins = {"http://localhost:3000"})
public class FollowersController {
    @Autowired
    private final FollowersService service;

    @Autowired
    private final AppUserService appUserService;

    public FollowersController(FollowersService followersService, AppUserService appUserService){
        this.service = followersService;
        this.appUserService = appUserService;
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(Principal principal){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<List<Long>> result = service.getFollowers(userId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(Principal principal){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<List<Long>> result = service.getFollowing(userId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/start-following")
    public ResponseEntity<?> startFollowing(
            Principal principal,
            @RequestBody String followerUsername
    ){
        String followingUsername = principal.getName();
        long followingUserId = appUserService.findByUsername(followingUsername).getUserId();
        long followerUserId = appUserService.findByUsername(followerUsername).getUserId();

        Result<Boolean> result = service.startFollowing(followerUserId, followingUserId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PostMapping("/stop-following")
    public ResponseEntity<?> stopFollowing(
            Principal principal,
            @RequestBody String followerUsername
    ){
        String followingUsername = principal.getName();
        long followingUserId = appUserService.findByUsername(followingUsername).getUserId();
        long followerUserId = appUserService.findByUsername(followerUsername).getUserId();

        Result<Boolean> result = service.stopFollowing(followerUserId, followingUserId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }
}
