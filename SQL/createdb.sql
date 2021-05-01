DROP SCHEMA IF EXISTS ShareBoard;
CREATE SCHEMA IF NOT EXISTS ShareBoard;

use ShareBoard;

CREATE TABLE user (
	id int AUTO_INCREMENT PRIMARY KEY,
    username varchar(30) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    email varchar(50) UNIQUE NOT NULL,
    descrizione varchar(255),
    immagine varchar(50),
    creation_date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE admin (
	id int PRIMARY KEY,
    FOREIGN KEY(id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE category (
	id int AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(100),
    name varchar(50) UNIQUE NOT NULL
);

CREATE TABLE follow(
	user_id int, 
    category_id int, 
    FOREIGN KEY (user_id) REFERENCES user(id),
	FOREIGN KEY (category_id) REFERENCES category(id),
    PRIMARY KEY(user_id, category_id)
);

CREATE TABLE post (
	post_id int AUTO_INCREMENT PRIMARY KEY,
    category_id int NOT NULL,
	author_id int NOT NULL,
    title VARCHAR(100) NOT NULL,
    text VARCHAR(1000) NOT NULL,
	#karma int DEFAULT 0,
    type VARCHAR(8) DEFAULT 'TEXT',
	creation_date TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY(author_id) REFERENCES user(id),
    FOREIGN KEY(category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE comment(
	id int AUTO_INCREMENT PRIMARY KEY,
    post_id int NOT NULL,
	author_id int NOT NULL,
    text varchar(1000) NOT NULL,
    #karma int DEFAULT 0,
    creation_date TIMESTAMP DEFAULT NOW(),
	FOREIGN KEY(author_id) REFERENCES user(id),
    FOREIGN KEY(post_id) REFERENCES post(post_id) ON DELETE CASCADE
);


CREATE TABLE commentParent(
	comment_id int,
    parent_id int,
    FOREIGN KEY(comment_id) REFERENCES comment(id),
    FOREIGN KEY(parent_id) REFERENCES comment(id),
    PRIMARY KEY(comment_id)
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

CREATE TABLE ban (
	id int AUTO_INCREMENT PRIMARY KEY,
    admin_id int NOT NULL,
    category_id int, 
    user_id int NOT NULL,
    data_inizio TIMESTAMP DEFAULT NOW(),
    data_fine TIMESTAMP,
    isGlobal tinyint,
	FOREIGN KEY(admin_id) REFERENCES admin(id),
	FOREIGN KEY(user_id) REFERENCES user(id),
	FOREIGN KEY(category_id) REFERENCES category(id)
);
