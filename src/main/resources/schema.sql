create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment NOT NULL
        primary key,
    FILM_NAME    CHARACTER VARYING(50) NOT NULL,
    DESCRIPTION  CHARACTER VARYING(500),
    RELEASE_DATE DATE NOT NULL,
    DURATION     INTEGER,
    MPA_ID       INTEGER
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment NOT NULL
        primary key,
    EMAIL     CHARACTER VARYING(50) NOT NULL,
    LOGIN     CHARACTER VARYING(50) NOT NULL,
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE NOT NULL
);

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER NOT NULL,
    MPA_NAME CHARACTER VARYING(5),
    constraint MPA_PK
        primary key (MPA_ID)
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER NOT NULL,
    GENRE_NAME CHARACTER VARYING(20),
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    constraint "FILM_GENRES_pk"
        primary key (FILM_ID, GENRE_ID),
    constraint "FILM_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
);

create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER NOT NULL,
    USER_ID INTEGER NOT NULL,
    constraint "LIKES_pk"
        primary key (FILM_ID, USER_ID),
    constraint "LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER NOT NULL,
    FRIEND_ID INTEGER NOT NULL,
    STATUS    ENUM ('CONFIRMED', 'UNCONFIRMED') NOT NULL,
    constraint FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint "FRIENDS_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIENDS_USERS_FRIEND_ID_fk"
        foreign key (FRIEND_ID) references USERS
);