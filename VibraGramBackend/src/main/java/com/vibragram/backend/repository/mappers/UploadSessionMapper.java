package com.vibragram.backend.repository.mappers;

import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.model.UploadSessionStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UploadSessionMapper implements RowMapper<UploadSession> {

    @Override
    public UploadSession mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID uploadSessionId = UUID.fromString(rs.getString("upload_session_id"));
        long userId = rs.getLong("user_id");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime expiresAt = rs.getTimestamp("expires_at").toLocalDateTime();
        UploadSessionStatus status = UploadSessionStatus.getStatusFromString(rs.getString("status"));
        long postId = rs.getLong("post_id");

        return new UploadSession(
                uploadSessionId,
                userId,
                createdAt,
                expiresAt,
                status,
                postId
        );
    }
}
