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

    @GetMapping("/following")//get people you follow
    public ResponseEntity<?> getFollowing(Principal principal){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<List<String>> result = service.getFollowers(userId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @GetMapping("/followers")//get people following you
    public ResponseEntity<?> getFollowers(Principal principal){
        String username = principal.getName();
        long userId = appUserService.findByUsername(username).getUserId();

        Result<List<String>> result = service.getFollowing(userId);
        if(result.isSuccess()){
            return ResponseEntity.ok(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PutMapping("/start-following/{followerUsername}")
    public ResponseEntity<?> startFollowing(
            Principal principal,
            @PathVariable String followerUsername
    ){
        String followingUsername = principal.getName();
        if(followerUsername.equals(followingUsername)){
            return ResponseEntity.badRequest().body("Cannot follow yourself");
        }
        long followingUserId = appUserService.findByUsername(followingUsername).getUserId();
        long followerUserId = appUserService.findByUsername(followerUsername).getUserId();

        Result<Boolean> result = service.startFollowing(followerUserId, followingUserId);
        if(result.isSuccess()){
            return ResponseEntity.status(201).body(result.getPayload());
        } else {
            return ErrorResponse.build(result);
        }
    }

    @PutMapping("/stop-following/{followerUsername}")
    public ResponseEntity<?> stopFollowing(
            Principal principal,
            @PathVariable String followerUsername
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
