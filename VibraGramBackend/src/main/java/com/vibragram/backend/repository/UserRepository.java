package com.vibragram.backend.repository;

import com.vibragram.backend.model.Gender;
import com.vibragram.backend.model.GenderUpdateRequest;

public interface UserRepository {
    boolean updateProfilePhoto(long id, String profilePicUrl);

    String findProfilePhotoUrl(long id);

    boolean updateBio(long id, String bio);

    boolean updateFullName(long id, String fullName);

    boolean updateGender(long id, Gender gender);
}
