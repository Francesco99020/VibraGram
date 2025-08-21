package com.vibragram.backend.repository;

import com.vibragram.backend.model.Gender;
import com.vibragram.backend.model.GenderUpdateRequest;
import com.vibragram.backend.model.User;
import com.vibragram.backend.repository.mappers.AppUserMapper;
import com.vibragram.backend.repository.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcTemplateRepository implements UserRepository{
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean updateProfilePhoto(long id, String profilePicUrl) {
        final String sql = "update users set " +
                "profile_pic = ? " +
                "where user_id = ?";

        return jdbcTemplate.update(sql, profilePicUrl, id) > 0;
    }

    @Override
    public String findProfilePhotoUrl(long id) {
        final String sql = "select * " +
                "from users " +
                "where user_id = ?";

        return jdbcTemplate.query(sql, new UserMapper(), id).stream()
                .findFirst().map(User::getProfilePic).orElse(null);
    }

    @Override
    public boolean updateBio(long id, String bio) {
        final String sql = "update users set " +
                "bio = ? " +
                "where user_id = ?";

        return jdbcTemplate.update(sql, bio, id) > 0;
    }

    @Override
    public boolean updateFullName(long id, String fullName) {
        final String sql = "update users set " +
                "full_name = ? " +
                "where user_id = ?";

        return jdbcTemplate.update(sql, fullName, id) > 0;
    }

    @Override
    public boolean updateGender(long id, Gender gender) {
        final String sql = "update users set " +
                "gender = ? " +
                "where user_id = ?";

        return jdbcTemplate.update(sql, gender.getDbValue(), id) > 0;
    }
}
