CREATE DATABASE IF NOT EXISTS moviedb;
USE moviedb;

CREATE TABLE movies (
                        id VARCHAR(10) NOT NULL,
                        title VARCHAR(100) NOT NULL,
                        year INTEGER NOT NULL,
                        director VARCHAR(100) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE stars (
                       id VARCHAR(10) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       birth_year INTEGER DEFAULT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE genres (
                        id INTEGER NOT NULL AUTO_INCREMENT,
                        name VARCHAR(32) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE credit_cards (
                              id VARCHAR(20) NOT NULL,
                              first_name VARCHAR(50) NOT NULL,
                              last_name VARCHAR(50) NOT NULL,
                              expiration DATE NOT NULL,
                              PRIMARY KEY (id)
);

CREATE TABLE customers (
                           id INTEGER NOT NULL AUTO_INCREMENT,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           credit_card_id VARCHAR(20) NOT NULL,
                           address VARCHAR(200) NOT NULL,
                           email VARCHAR(50) NOT NULL,
                           password VARCHAR(20) NOT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (credit_card_id) REFERENCES credit_cards(id)
);

CREATE TABLE stars_in_movies (
                                 star_id VARCHAR(10) NOT NULL,
                                 movie_id VARCHAR(10) NOT NULL,
                                 PRIMARY KEY (star_id, movie_id),
                                 FOREIGN KEY (star_id) REFERENCES stars(id),
                                 FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE genres_in_movies (
                                  genre_id INTEGER NOT NULL,
                                  movie_id VARCHAR(10) NOT NULL,
                                  PRIMARY KEY (genre_id, movie_id),
                                  FOREIGN KEY (genre_id) REFERENCES genres(id),
                                  FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE sales (
                       id INTEGER NOT NULL AUTO_INCREMENT,
                       customer_id INTEGER NOT NULL,
                       movie_id VARCHAR(10) NOT NULL,
                       sale_date DATE NOT NULL,
                       PRIMARY KEY (id),
                       FOREIGN KEY (customer_id) REFERENCES customers(id),
                       FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE ratings (
                         movie_id VARCHAR(10) NOT NULL,
                         rating FLOAT NOT NULL,
                         vote_count INTEGER NOT NULL,
                         PRIMARY KEY (movie_id),
                         FOREIGN KEY (movie_id) REFERENCES movies(id)
);