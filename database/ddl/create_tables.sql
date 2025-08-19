drop database if exists vibragram_test;
create database vibragram_test;
use vibragram_test;

-- create tables and relationships

create table users (
	user_id bigint primary key auto_increment,
    username varchar(30) unique,
    email varchar(255) unique,
    password_hash varchar(255),
    full_name varchar(100),
    bio text,
    profile_pic varchar(255),
    is_admin boolean,
    created_at timestamp default now(),
    updated_at timestamp default current_timestamp on update current_timestamp
);

create index username_index on users (username, user_id);

create table followers (
    follower_id bigint,
    following_id bigint,
    created_at timestamp default now(),
    constraint pk_followers primary key(follower_id, following_id),
    constraint fk_followers_follower_id foreign key (follower_id) references users(user_id),
    constraint fk_followers_following_id foreign key (following_id) references users(user_id)
);

CREATE INDEX idx_followers_follower_id ON followers(follower_id);
CREATE INDEX idx_followers_following_id ON followers(following_id);
CREATE UNIQUE INDEX idx_followers_pair ON followers(follower_id, following_id);

create table posts (
	post_id bigint primary key auto_increment,
    user_id bigint,
    caption text,
    created_at timestamp default now(),
    constraint fk_posts_user_id foreign key (user_id) references users(user_id)
);

CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_user_date ON posts(user_id, created_at DESC);

create table comments (
    comment_id bigint primary key auto_increment,
    post_id bigint,
    user_id bigint,
    content text,
    created_at timestamp default now(),
    constraint fk_comments_post_id foreign key (post_id) references posts(post_id),
    constraint fk_comments_user_id foreign key (user_id) references users(user_id)
);

create table media (
	media_id bigint primary key auto_increment,
    post_id bigint,
    media_type enum('image','video'),
    media_url varchar(255),
    media_order int,
    created_at timestamp default now(),
    constraint fk_media_post_id foreign key (post_id) references posts(post_id)
);

CREATE INDEX idx_media_post_id ON media(post_id);

create table tags (
	tag_id bigint primary key auto_increment,
    media_id bigint,
    user_id bigint,
    position_x decimal(5,2),
    position_y decimal(5,2),
    created_at timestamp default now(),
    constraint fk_tags_media_id foreign key (media_id) references media(media_id),
    constraint fk_tags_user_id foreign key (user_id) references users(user_id)
);

create table hashtags (
	hashtag_id bigint primary key auto_increment,
    tag_text varchar(100) unique,
    created_at timestamp default now()
);

create table post_hashtags (
	post_id bigint,
    hashtag_id bigint,
    constraint fk_post_hashtags_post_id foreign key (post_id) references posts(post_id),
    constraint fk_post_hashtags_hashtag_id foreign key (hashtag_id) references hashtags(hashtag_id)
);

create table likes (
	post_id bigint,
    user_id bigint,
    created_at timestamp default now(),
    constraint pk_likes primary key(post_id, user_id),
    constraint fk_likes_post_id foreign key (post_id) references posts(post_id),
    constraint fk_likes_user_id foreign key (user_id) references users(user_id)
);

create table messages (
    message_id bigint primary key auto_increment,
    sender_id bigint,
    receiver_id bigint,
    content text,
    created_at timestamp default now(),
    constraint fk_messages_sender foreign key (sender_id) references users(user_id),
    constraint fk_messages_receiver foreign key (receiver_id) references users(user_id)
);

create table notifications (
    notification_id bigint primary key auto_increment,
    user_id bigint, -- recipient
    actor_id bigint, -- who triggered the notification
    type enum('like','comment','follow','mention'),
    post_id bigint null,
    created_at timestamp default now(),
    is_read boolean default false,
    constraint fk_notifications_user foreign key (user_id) references users(user_id),
    constraint fk_notifications_actor foreign key (actor_id) references users(user_id),
    constraint fk_notifications_post foreign key (post_id) references posts(post_id)
);

