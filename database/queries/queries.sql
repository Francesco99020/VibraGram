use vibragram_test;

-- Get all users
SELECT * FROM users;

-- Get a specific user by username
SELECT * FROM users WHERE username = 'alice';

-- See who "alice" is following
SELECT u.username AS following
FROM followers f
JOIN users u ON f.following_id = u.user_id
WHERE f.follower_id = (SELECT user_id FROM users WHERE username = 'alice');

-- See who follows "bob"
SELECT u.username AS follower
FROM followers f
JOIN users u ON f.follower_id = u.user_id
WHERE f.following_id = (SELECT user_id FROM users WHERE username = 'bob');


-- Get posts from a specific user (bob)
SELECT p.post_id, p.caption, p.created_at
FROM posts p
JOIN users u ON p.user_id = u.user_id
WHERE u.username = 'bob'
ORDER BY p.created_at DESC;

-- Get posts that include media
SELECT p.post_id, p.caption, m.media_url
FROM posts p
JOIN media m ON m.post_id = p.post_id
ORDER BY p.created_at DESC;


-- See who liked post #1
SELECT u.username
FROM likes l
JOIN users u ON l.user_id = u.user_id
WHERE l.post_id = 1;

-- See which posts "alice" has liked
SELECT p.post_id, p.caption
FROM likes l
JOIN posts p ON l.post_id = p.post_id
WHERE l.user_id = (SELECT user_id FROM users WHERE username = 'alice');


-- Get all comments on post #1
SELECT u.username, c.content, c.created_at
FROM comments c
JOIN users u ON c.user_id = u.user_id
WHERE c.post_id = 1
ORDER BY c.created_at;

-- Get all comments made by "bob"
SELECT c.content, p.post_id AS post_id
FROM comments c
JOIN posts p ON c.post_id = p.post_id
WHERE c.user_id = (SELECT user_id FROM users WHERE username = 'bob');


-- This should use the index on posts(user_id)
EXPLAIN ANALYZE
SELECT * FROM posts WHERE user_id = 2;

-- This should use the index on followers(follower_id)
EXPLAIN ANALYZE
SELECT * FROM followers WHERE follower_id = 1;