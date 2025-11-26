package com.vibragram.backend.repository;

import java.util.List;

public interface FollowersRepository {
    public List<Long> getFollowers(long userId);

    public List<Long> getFollowing(long userId);

    public boolean startFollowing(long follower, long following);

    public boolean stopFollowing(long follower, long following);
}
