package com.vibragram.backend.repository;

public interface UserRepository {
    boolean updateProfilePhoto(long id, String profilePicUrl);
}
