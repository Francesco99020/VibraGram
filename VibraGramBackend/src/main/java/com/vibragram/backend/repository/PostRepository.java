package com.vibragram.backend.repository;

import com.vibragram.backend.model.Post;

public interface PostRepository {
    public Long createPost(Post post);

    public Post getPostById(long postId);
}
