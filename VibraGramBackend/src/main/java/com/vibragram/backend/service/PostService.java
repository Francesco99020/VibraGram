package com.vibragram.backend.service;

import com.vibragram.backend.model.Post;
import com.vibragram.backend.model.Request.PostRequest;
import com.vibragram.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private final PostRepository repository;

    @Autowired
    private final  MediaService mediaService;


    public PostService(PostRepository repository, MediaService mediaService) {
        this.repository = repository;
        this.mediaService = mediaService;
    }


    public Result<Post> createPost(PostRequest request, UUID uploadSessionId, long userId){
        //create result
        Result<Post> result = new Result<>();

        //create a post
        Post post = new Post();
        post.setUserId(userId);
        post.setCaption(request.getCaption());
        post.setLocation(request.getLocation());

        // Pass down to repo to save
        Long postId = repository.createPost(post);
        if(postId == null){//if failed to save
            result.addMessage("Post could not be created", ResultType.INVALID);
            return result;
        }

        //get newly created post
        Result<Post> postResult = getPostById(postId);

        if(!postResult.isSuccess()){
            result.addMessage("Post could not be fetched", ResultType.NOT_FOUND);
            return result;
        }

        post = postResult.getPayload();
        //find all media with uploadSessionId and set postId
        if(!mediaService.linkMediaToPost(postId, uploadSessionId)){
            result.addMessage("Media could not be updated.", ResultType.INVALID);
            return result;
        }

        // Expire uploadSessionId for current post to allow user to create multiple posts back to back
        if(!mediaService.expireUploadSessionId(uploadSessionId)){
            result.addMessage("Could not expire uploadSessionId", ResultType.INVALID);
            return result;
        }

        result.setPayload(post);
        return result;
    }

    public Result<Post> getPostById(long postId){
        Result<Post> result = new Result<>();

        Post post = repository.getPostById(postId);
        if(post == null){
            result.addMessage("Post with id " + postId + " not found.", ResultType.NOT_FOUND);
        } else {
            result.setPayload(post);
        }
        return result;
    }
}
