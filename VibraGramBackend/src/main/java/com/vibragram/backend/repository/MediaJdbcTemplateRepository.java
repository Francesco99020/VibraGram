package com.vibragram.backend.repository;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.model.UploadSession;
import com.vibragram.backend.repository.mappers.MediaMapper;
import com.vibragram.backend.repository.mappers.UploadSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
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
                "(upload_session_id, user_id, created_at, expires_at) " +
                "values (?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, uploadSession.getUploadSessionId().toString());
            ps.setLong(2, uploadSession.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(uploadSession.getCreatedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(uploadSession.getExpiresAt()));
            return ps;
        }, keyHolder);

        return rowsAffected > 0;
    }

    @Override
    public UploadSession getUploadSessionByUUID(UUID uploadSessionId) {
        final String sql = "select * from upload_session " +
                "where upload_session_id = ? " +
                "order by expires_at desc " +
                "limit 1;";

        return jdbcTemplate.query(sql, new UploadSessionMapper(), uploadSessionId.toString()).stream()
                .findFirst().orElse(null);
    }

    @Override
    public UploadSession getUploadSessionByUserId(long userId) {
        final String sql = "select * from upload_session " +
                "where user_id = ? " +
                "order by expires_at desc " +
                "limit 1;";

        return jdbcTemplate.query(sql, new UploadSessionMapper(), userId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Media getMediaByMediaURL(String url) {
        final String sql = "select * " +
                "from media " +
                "where media_url = ?";

        return jdbcTemplate.query(sql, new MediaMapper(), url).stream().findFirst().orElse(null);
    }

    @Override
    public Long getUserIdOfUploadSession(UUID uploadSessionId) {
        final String sql = "select * " +
                "from upload_session " +
                "where upload_session_id = ?";

        UploadSession uploadSession = jdbcTemplate.query(sql, new UploadSessionMapper(), uploadSessionId.toString()).stream()
                .findFirst().orElse(null);

        return (uploadSession == null) ? null : uploadSession.getUserId();
    }

    @Override
    public boolean createMedia(Media media) {
        final String sql = "insert into media " +
                "(media_type, media_url, media_order, upload_session_id) " +
                "values (?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, media.getMediaType().getStringName());
            ps.setString(2, media.getMediaUrl());
            ps.setInt(3, media.getMediaOrder());
            ps.setString(4, media.getUploadSessionId().toString());

            return ps;
        }, keyHolder);

        if(rowsAffected > 0){
            Number key = keyHolder.getKey();
            if(key != null){
                media.setMediaId(key.longValue());
            } else {
                media.setMediaId((long) -1);
            }
        }

        return rowsAffected > 0;
    }

    @Override
    public List<Media> getMediaByUploadSession(UUID uploadSessionId) {
        final String sql = "select * " +
                "from media " +
                "where upload_session_id = ?";

        return jdbcTemplate.query(sql, new MediaMapper(), uploadSessionId.toString());
    }

    @Override
    public List<Media> getMediaByPostId(long postId) {
        final String sql = "select * " +
                "from media " +
                "where post_id = ?";

        return jdbcTemplate.query(sql, new MediaMapper(), postId);
    }

    @Override
    public boolean updateMedia(Media media) {
        final String sql = "update media set " +
                "post_id = ?, " +
                "media_type = ?, " +
                "media_url = ?, " +
                "media_order = ? " +
                "where media_id = ?";

        return jdbcTemplate.update(sql, media.getPostId(),
                media.getMediaType().getStringName(), media.getMediaUrl(), media.getMediaOrder(), media.getMediaId()) > 0;
    }

    @Override
    public boolean expireUploadSessionId(UUID uploadSessionId) {
        final String sql = "update upload_session set " +
                "expires_at = NOW() " +
                "where upload_session_id = ?";

        return jdbcTemplate.update(sql, uploadSessionId.toString()) > 0;
    }

    @Override
    public boolean deleteMedia(long mediaId) {
        final String sql = "delete from media " +
                "where media_id = ?";

        return jdbcTemplate.update(sql, mediaId) > 0;
    }
}
