create  table book if  not exist book(
    book_id             INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_name        VARCHAR(128)  NOT NULL,
    author               VARCHAR(32)    NOT NULL,
    catagory            VARCHAR(32)    NOT NULL,
    price                 INT                    NOT NULL,
    description        VARCHAR(1024) NOT NULL,
    stock                 INT                   NOT NULL,
    `language`         VARCHAR(32)    NOT NULL,
    publication_date TIMESTAMP     NOT NULL
);

create table if  not exist user(
    user_id                        INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email                           VARCHAR(750)    NOT NULL UNIQUE,
    passwd                        VARCHAR(512)    NOT NULL,
    role                              VARCHAR(128)    NOT NULL,
    create_date                  TIMESTAMP    NOT NULL,
    last_modify_date          TIMESTAMP    NOT NULL
);