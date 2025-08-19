package com.vibragram.backend.repository.mappers;

import com.vibragram.backend.model.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserMapper implements RowMapper<AppUser> {

    @Override
    public AppUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String hashedPassword = resultSet.getString("password_hash");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        boolean isAdmin = resultSet.getBoolean("is_admin");

        return new AppUser(
                userId,
                username,
                email,
                hashedPassword,
                createdAt,
                isAdmin,
                true
        );
    }
}
