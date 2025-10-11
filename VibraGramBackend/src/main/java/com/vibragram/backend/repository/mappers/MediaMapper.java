package com.vibragram.backend.repository.mappers;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.MediaType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MediaMapper implements RowMapper<Media> {
    @Override
    public Media mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long mediaId = rs.getLong("media_id");
        UUID uploadSessionId = UUID.fromString(rs.getString("upload_session_id"));
        Long postId = rs.getLong("post_id");
        MediaType mediaType = MediaType.valueOf(rs.getString("media_type").toUpperCase());
        String mediaUrl = rs.getString("media_url");
        int mediaOrder = rs.getInt("media_order");

        return new Media(
                mediaId,
                uploadSessionId,
                postId,
                mediaType,
                mediaUrl,
                mediaOrder
        );
    }
}
