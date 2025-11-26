package com.vibragram.backend.service;

import com.vibragram.backend.repository.FollowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowersService {

    @Autowired
    private final FollowersRepository repository;

    public FollowersService(FollowersRepository repository){
        this.repository = repository;
    }

    public Result<List<Long>> getFollowers(long userId){
        Result<List<Long>> result = new Result<>();
        List<Long> followers = repository.getFollowers(userId);
        if(followers.isEmpty()){
            result.addMessage("No followers found for user.", ResultType.NOT_FOUND);
        } else {
            result.setPayload(followers);
        }
        return result;
    }

    public Result<List<Long>> getFollowing(long userId){
        Result<List<Long>> result = new Result<>();
        List<Long> following = repository.getFollowing(userId);
        if(following.isEmpty()){
            result.addMessage("No following found for user.", ResultType.NOT_FOUND);
        } else {
            result.setPayload(following);
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
