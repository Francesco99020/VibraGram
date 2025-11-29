package com.vibragram.backend.service;

import com.vibragram.backend.repository.FollowersRepository;
import com.vibragram.backend.security.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowersService {

    @Autowired
    private final FollowersRepository repository;

    @Autowired
    private final AppUserService appUserService;

    public FollowersService(FollowersRepository repository, AppUserService appUserService){
        this.repository = repository;
        this.appUserService = appUserService;
    }

    public Result<List<String>> getFollowers(long userId){
        Result<List<String>> result = new Result<>();
        List<Long> followers = repository.getFollowers(userId);
        if(followers == null){
            result.addMessage("User not found.", ResultType.NOT_FOUND);
        } else if (followers.isEmpty()) {
            result.setPayload(List.of("No followers found for user."));
        } else {
            List<String> followersUsernameList = new ArrayList<>();
            for (Long user : followers){
                followersUsernameList.add(appUserService.findById(user.intValue()).getUsername());
            }
            result.setPayload(followersUsernameList);
        }
        return result;
    }

    public Result<List<String>> getFollowing(long userId){
        Result<List<String>> result = new Result<>();
        List<Long> following = repository.getFollowing(userId);
        if(following == null){
            result.addMessage("User not found.", ResultType.NOT_FOUND);
        } else if (following.isEmpty()) {
            result.setPayload(List.of("No following found for user."));
            return result;
        } else {
            List<String> followingUsernameList = new ArrayList<>();
            for (Long user : following){
                followingUsernameList.add(appUserService.findById(user.intValue()).getUsername());
            }
            result.setPayload(followingUsernameList);
        }
        return result;
    }

    public Result<Boolean> startFollowing(long follower, long following){
        Result<Boolean> result = new Result<>();
        boolean isFollowing = repository.startFollowing(follower, following);
        if(isFollowing){
            result.setPayload(true);
        } else {
            result.addMessage("Could not follower user.", ResultType.INVALID);
        }
        return result;
    }

    public Result<Boolean> stopFollowing(long follower, long following){
        Result<Boolean> result = new Result<>();
        boolean isNotFollowing = repository.stopFollowing(follower, following);
        if(isNotFollowing){
            result.setPayload(true);
        } else {
            result.addMessage("Could not unfollow user.", ResultType.INVALID);
        }
        return result;
    }
}
