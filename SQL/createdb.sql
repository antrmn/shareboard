DROP SCHEMA IF EXISTS ShareBoard;
CREATE SCHEMA IF NOT EXISTS ShareBoard;

use ShareBoard;

CREATE TABLE user (
	id int AUTO_INCREMENT PRIMARY KEY,
    username varchar(30) UNIQUE NOT NULL,
    password varchar(50) NOT NULL,
    email varchar(50) UNIQUE NOT NULL,
    creation_date date DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
	admin_id int PRIMARY KEY,
    FOREIGN KEY(admin_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE category (
	id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) UNIQUE NOT NULL
);

CREATE TABLE post (
	post_id int AUTO_INCREMENT PRIMARY KEY,
    category_id int NOT NULL,
	author_id int NOT NULL,
    title VARCHAR(100) NOT NULL,
    text VARCHAR(255) NOT NULL,
	karma int DEFAULT 0,
    type VARCHAR(10) DEFAULT 'text',
    creation_date date DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(author_id) REFERENCES user(id),
    FOREIGN KEY(category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE comment(
	id int AUTO_INCREMENT PRIMARY KEY,
    post_id int NOT NULL,
	author_id int NOT NULL,
    text varchar(1000) NOT NULL,
    karma int DEFAULT 0,
    creation_date date DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY(author_id) REFERENCES user(id),
    FOREIGN KEY(post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE postVotes (
	post_id int NOT NULL,
    user_id int NOT NULL,
    vote tinyint NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(post_id) REFERENCES post(post_id) ON DELETE CASCADE,
    PRIMARY KEY(post_id, user_id)
);

CREATE TABLE commentVotes (
	comment_id int NOT NULL,
    user_id int NOT NULL,
    vote tinyint NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(comment_id) REFERENCES comment(id) ON DELETE CASCADE,
    PRIMARY KEY(comment_id, user_id)
);
