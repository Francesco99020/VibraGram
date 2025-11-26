package com.vibragram.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class FollowersJdbcTemplateRepository implements FollowersRepository{
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FollowersJdbcTemplateRepository(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}


    @Override
    public List<Long> getFollowers(long userId) {
        final String sql = "select follower_id from followers " +
                "where following_id = ?";

        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public List<Long> getFollowing(long userId) {
        final String sql = "select following_id from followers " +
                "where follower_id = ?";

        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public boolean startFollowing(long follower, long following) {
        final String sql = "insert into followers " +
                "(follower_id, following_id) " +
                "values (?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, follower);
            ps.setLong(2, following);
            return ps;
        }, keyHolder);

        return rowsAffected > 0;
    }

    @Override
    public boolean stopFollowing(long follower, long following) {
        return jdbcTemplate.update("delete from followers where follower_id = ? AND following_id = ?", follower, following) > 0;
    }
}
