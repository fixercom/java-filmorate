create table FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    FILM_NAME    CHARACTER VARYING(50),
    DESCRIPTION  CHARACTER VARYING(500),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER
);

create table USERS
(
    USER_ID   INTEGER auto_increment
        primary key,
    EMAIL     CHARACTER VARYING(50),
    LOGIN     CHARACTER VARYING(50),
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE
);

create table MPA
(
    MPA_ID   INTEGER,
    MPA_NAME CHARACTER VARYING(5),
    constraint MPA_PK
        primary key (MPA_ID)
);

create table GENRES
(
    GENRE_ID   INTEGER,
    GENRE_NAME CHARACTER VARYING(20),
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_pk"
        primary key (FILM_ID, GENRE_ID),
    constraint "FILM_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
);

create table LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "LIKES_pk"
        primary key (FILM_ID, USER_ID),
    constraint "LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
);

create table FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    ENUM ('CONFIRMED', 'UNCONFIRMED') not null,
    constraint FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint "FRIENDS_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIENDS_USERS_FRIEND_ID_fk"
        foreign key (FRIEND_ID) references USERS
);