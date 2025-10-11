package com.vibragram.backend.repository.mappers;

import com.vibragram.backend.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        long postId = rs.getLong("post_id");
        long userId = rs.getLong("user_id");
        String caption = rs.getString("caption");
        String location = rs.getString("location");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return new Post(
                postId,
                userId,
                caption,
                location,
                createdAt
        );
    }
}
