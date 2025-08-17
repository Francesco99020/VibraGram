-- Seed data for VibraGram
use vibragram_test;

-- USERS
INSERT INTO users (username, email, password_hash, full_name, bio, profile_pic) VALUES
('alice', 'alice@example.com', 'hashed_pw1', 'Alice Johnson', 'Love traveling and photography.', '/images/alice.jpg'),
('bob', 'bob@example.com', 'hashed_pw2', 'Bob Smith', 'Coffee addict. Developer.', '/images/bob.jpg'),
('carol', 'carol@example.com', 'hashed_pw3', 'Carol White', 'Music is life.', '/images/carol.jpg'),
('dave', 'dave@example.com', 'hashed_pw4', 'Dave Brown', 'Foodie & Gamer.', '/images/dave.jpg');

-- FOLLOWERS (Alice follows Bob, Carol follows Alice, Bob follows Dave)
INSERT INTO followers (follower_id, following_id) VALUES
(1, 2),
(3, 1),
(2, 4);

-- POSTS
INSERT INTO posts (user_id, caption) VALUES
(1, 'Exploring the mountains today! #adventure #travel'),
(2, 'Morning coffee ☕ #caffeine'),
(3, 'New song dropped! 🎶 #music #love'),
(4, 'Best ramen in town 🍜 #foodie');

-- MEDIA (linking media to posts)
INSERT INTO media (post_id, media_type, media_url, media_order) VALUES
(1, 'image', '/media/posts/1/mountain1.jpg', 1),
(1, 'image', '/media/posts/1/mountain2.jpg', 2),
(2, 'image', '/media/posts/2/coffee.jpg', 1),
(3, 'video', '/media/posts/3/song.mp4', 1),
(4, 'image', '/media/posts/4/ramen.jpg', 1);

-- TAGS (Alice tagged Bob in mountain photo, Carol tagged herself in music video)
INSERT INTO tags (media_id, user_id, position_x, position_y) VALUES
(1, 2, 50.00, 60.00),
(4, 3, 20.00, 30.00);

-- HASHTAGS
INSERT INTO hashtags (tag_text) VALUES
('adventure'),
('travel'),
('caffeine'),
('music'),
('love'),
('foodie');

-- POST_HASHTAGS (linking posts to hashtags)
INSERT INTO post_hashtags (post_id, hashtag_id) VALUES
(1, 1), (1, 2),   -- Alice's mountain post
(2, 3),           -- Bob's coffee post
(3, 4), (3, 5),   -- Carol's music post
(4, 6);           -- Dave's foodie post

-- COMMENTS
INSERT INTO comments (post_id, user_id, content) VALUES
(1, 2, 'Wow! That view looks amazing 🔥'),
(1, 3, 'Take me with you next time!'),
(2, 1, 'Coffee looks great!'),
(3, 4, 'Your voice is incredible 👏'),
(4, 1, 'I need this ramen in my life.');

-- LIKES
INSERT INTO likes (post_id, user_id) VALUES
(1, 2),
(1, 3),
(2, 1),
(3, 1),
(3, 4),
(4, 2);

-- MESSAGES
INSERT INTO messages (sender_id, receiver_id, content) VALUES
(1, 2, 'Hey Bob, want to go hiking this weekend?'),
(2, 1, 'Sure Alice, sounds great!'),
(3, 4, 'Dave, check out my new track.'),
(4, 3, 'On it!');

-- NOTIFICATIONS
INSERT INTO notifications (user_id, actor_id, type, post_id) VALUES
(1, 2, 'comment', 1), -- Bob commented on Alice's post
(1, 3, 'like', 1),    -- Carol liked Alice's post
(2, 1, 'like', 2),    -- Alice liked Bob's coffee post
(3, 4, 'comment', 3); -- Dave commented on Carol's song
