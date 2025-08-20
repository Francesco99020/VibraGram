package com.vibragram.backend.repository;

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
}
