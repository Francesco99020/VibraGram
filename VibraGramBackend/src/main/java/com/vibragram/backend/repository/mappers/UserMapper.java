package com.vibragram.backend.repository.mappers;

import com.vibragram.backend.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String fullName = rs.getString("full_name");
        String bio = rs.getString("bio");
        String profilePic = rs.getString("profile_pic");
        boolean isAdmin = rs.getBoolean("is_admin");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        return new User(
                userId,
                username,
                email,
                fullName,
                bio,
                profilePic,
                isAdmin,
                createdAt,
                updatedAt
        );
    }
}
