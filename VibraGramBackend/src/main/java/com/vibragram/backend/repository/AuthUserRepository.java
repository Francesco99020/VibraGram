package com.vibragram.backend.repository;

import com.vibragram.backend.model.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface AuthUserRepository {
    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    AppUser findById(int userId);

    AppUser add(AppUser appUser);

    boolean update(AppUser appUser);

    @Transactional
    boolean deleteById(int userId);
}
