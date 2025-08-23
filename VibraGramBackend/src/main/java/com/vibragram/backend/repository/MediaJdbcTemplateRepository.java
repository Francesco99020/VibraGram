package com.vibragram.backend.repository;

import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.repository.mappers.UploadSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;

@Repository
public class MediaJdbcTemplateRepository implements MediaRepository{
    @Autowired
    private final JdbcTemplate jdbcTemplate;


    public MediaJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean createUploadSession(UploadSession uploadSession) {
        final String sql = "insert into upload_session " +
                "(upload_session_id, user_id, created_at, expires_at, status, post_id) " +
                "values (?, ?, ?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, uploadSession.getUploadSessionId().toString());
            ps.setLong(2, uploadSession.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(uploadSession.getCreatedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(uploadSession.getExpiresAt()));
            ps.setString(5, uploadSession.getStatus().getMessage());

            if (uploadSession.getPostId() == 0) {
                ps.setNull(6, Types.BIGINT);
            } else {
                ps.setLong(6, uploadSession.getPostId());
            }
            return ps;
        }, keyHolder);

        return rowsAffected > 0;
    }

    @Override
    public UploadSession getUploadSessionByUUID(UUID uploadSessionId) {
        final String sql = "select * from upload_session " +
                "where upload_session_id = ? " +
                "and `status` = 'active' " +
                "and expires_at > NOW();";

        return jdbcTemplate.query(sql, new UploadSessionMapper(), uploadSessionId.toString()).stream()
                .findFirst().orElse(null);
    }

    @Override
    public UploadSession getUploadSessionByUserId(long userId) {
        final String sql = "select * from upload_session " +
                "where user_id = ? " +
                "and `status` = 'active' " +
                "and expires_at > NOW()";

        return jdbcTemplate.query(sql, new UploadSessionMapper(), userId).stream()
                .findFirst().orElse(null);
    }
}
