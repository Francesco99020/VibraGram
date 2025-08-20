package com.vibragram.backend.repository;

import com.vibragram.backend.model.AppUser;
import com.vibragram.backend.repository.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class AuthUserJdbcTemplateRepository implements AuthUserRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public AuthUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUser findByUsername(String username) {
        final String sql = "select user_id, username, email, password_hash, created_at, is_admin " +
                "from users " +
                "where username = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), username).stream()
                .findFirst().orElse(null);
    }

    @Override
    public AppUser findByEmail(String email) {
        final String sql = "select user_id, username, email, password_hash, created_at, is_admin " +
                "from users " +
                "where email = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), email).stream()
                .findFirst().orElse(null);
    }

    @Override
    public AppUser findById(int userId) {
        final String sql = "select user_id, username, email, password_hash, created_at, is_admin "
                + "from users "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new UserMapper(), userId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public AppUser add(AppUser appUser) {
        final String sql = "insert into users (username, email, password_hash, created_at, is_admin) values (?, ?, ?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, appUser.getUsername());
            ps.setString(2, appUser.getEmail());
            ps.setString(3, appUser.getPassword());
            ps.setDate(4, Date.valueOf(appUser.getCreatedAt().toLocalDate()));
            ps.setBoolean(5, appUser.isAdmin());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0){
            return null;
        }

        appUser.setUserId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return appUser;
    }

    @Override
    public boolean update(AppUser appUser) {
        final String sql = "update users set " +
                "username = ?, " +
                "email = ? " +
                "where users = ?;";

        return jdbcTemplate.update(sql, appUser.getUsername(), appUser.getEmail(), appUser.getUserId()) > 0;
    }

    @Transactional
    @Override
    public boolean deleteById(int userId) {
        // delete dependencies first
        jdbcTemplate.update("delete from notifications where user_id = ? or actor_id = ?", userId, userId);
        jdbcTemplate.update("delete from messages where sender_id = ? or receiver_id = ?", userId, userId);
        jdbcTemplate.update("delete from likes where user_id = ?", userId);
        jdbcTemplate.update("delete from comments where user_id = ?", userId);
        jdbcTemplate.update("delete from tags where user_id = ?", userId);
        jdbcTemplate.update("delete from followers where follower_id = ? or following_id = ?", userId, userId);

        // delete posts (and cascading media, hashtags, etc. if not cascade)
        jdbcTemplate.update("delete from post_hashtags where post_id in (select post_id from posts where user_id = ?)", userId);
        jdbcTemplate.update("delete from media where post_id in (select post_id from posts where user_id = ?)", userId);
        jdbcTemplate.update("delete from comments where post_id in (select post_id from posts where user_id = ?)", userId);
        jdbcTemplate.update("delete from likes where post_id in (select post_id from posts where user_id = ?)", userId);
        jdbcTemplate.update("delete from posts where user_id = ?", userId);

        // finally delete the user
        int rows = jdbcTemplate.update("delete from users where user_id = ?", userId);
        return rows > 0;
    }
}
