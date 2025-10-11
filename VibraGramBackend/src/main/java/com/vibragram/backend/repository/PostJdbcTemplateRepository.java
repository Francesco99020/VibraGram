package com.vibragram.backend.repository;

import com.vibragram.backend.model.Post;
import com.vibragram.backend.repository.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class PostJdbcTemplateRepository implements PostRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public PostJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long createPost(Post post) {
        final String sql = "insert into posts " +
                "(user_id, caption, location) " +
                "values (?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, post.getUserId());
            ps.setString(2, post.getCaption());
            ps.setString(3, post.getLocation());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Post getPostById(long postId) {
        final String sql = "select * from posts " +
                "where post_id = ?";

        return jdbcTemplate.query(sql, new PostMapper(), postId).stream()
                .findFirst().orElse(null);
    }
}
