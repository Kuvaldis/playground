CREATE TABLE author (
  id              NUMBER(7)     NOT NULL PRIMARY KEY,
  first_name      VARCHAR2(50),
  last_name       VARCHAR2(50)  NOT NULL,
  date_of_birth   DATE,
  year_of_birth   NUMBER(7),
  distinguished   NUMBER(1)
);

CREATE TABLE language (
  id              NUMBER(7)     NOT NULL PRIMARY KEY,
  cd              CHAR(2)       NOT NULL,
  description     VARCHAR2(50)
);

CREATE TABLE book (
  id              NUMBER(7)     NOT NULL PRIMARY KEY,
  author_id       NUMBER(7)     NOT NULL,
  title           VARCHAR2(400) NOT NULL,
  published_in    NUMBER(7)     NOT NULL,
  language_id     NUMBER(7)     NOT NULL,

  CONSTRAINT fk_book_author     FOREIGN KEY (author_id)   REFERENCES author(id),
  CONSTRAINT fk_book_language   FOREIGN KEY (language_id) REFERENCES language(id)
);

CREATE TABLE book_store (
  name            VARCHAR2(400) NOT NULL UNIQUE
);

CREATE TABLE book_to_book_store (
  name            VARCHAR2(400) NOT NULL,
  book_id         INTEGER       NOT NULL,
  stock           INTEGER,

  PRIMARY KEY(name, book_id),
  CONSTRAINT fk_b2bs_book_store FOREIGN KEY (name)        REFERENCES book_store (name) ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_book       FOREIGN KEY (book_id)     REFERENCES book (id)         ON DELETE CASCADE
);

INSERT INTO language (id, cd, description) VALUES (1, 'en', 'English');
INSERT INTO language (id, cd, description) VALUES (2, 'de', 'Deutsch');
INSERT INTO language (id, cd, description) VALUES (3, 'fr', 'Fran?ais');
INSERT INTO language (id, cd, description) VALUES (4, 'pt', 'Portugu?s');

INSERT INTO author (id, first_name, last_name  , date_of_birth    , year_of_birth)
  VALUES           (1,  'Howard'  , 'Lovecraft', DATE '1890-08-20', 1890         ),
                   (2,  'Mark'    , 'Twain'    , DATE '1871-02-07', 1871         ),
                   (3,  'Stephen' , 'King'     , DATE '1947-09-21', 1947         );
INSERT INTO author (id, first_name, last_name  , date_of_birth    , year_of_birth)
  VALUES           (4 , 'George'  , 'Orwell'   , DATE '1903-06-26', 1903         );
INSERT INTO author (id, first_name, last_name  , date_of_birth    , year_of_birth)
  VALUES           (5 , 'Paulo'   , 'Coelho'   , DATE '1947-08-24', 1947         );

INSERT INTO book (id, author_id, title         , published_in, language_id)
  VALUES         (1 , 4        , '1984'        , 1948        , 1          );
INSERT INTO book (id, author_id, title         , published_in, language_id)
  VALUES         (2 , 4        , 'Animal Farm' , 1945        , 1          );
INSERT INTO book (id, author_id, title         , published_in, language_id)
  VALUES         (3 , 5        , 'O Alquimista', 1988        , 4          );
INSERT INTO book (id, author_id, title         , published_in, language_id)
  VALUES         (4 , 5        , 'Brida'       , 1990        , 2          );

-- INSERT INTO book_store VALUES ('Orell F?ssli');
-- INSERT INTO book_store VALUES ('Ex Libris');
-- INSERT INTO book_store VALUES ('Buchhandlung im Volkshaus');

-- INSERT INTO t_book_to_book_store VALUES ('Orell F?ssli'             , 1, 10);
-- INSERT INTO t_book_to_book_store VALUES ('Orell F?ssli'             , 2, 10);
-- INSERT INTO t_book_to_book_store VALUES ('Orell F?ssli'             , 3, 10);
-- INSERT INTO t_book_to_book_store VALUES ('Ex Libris'                , 1, 1 );
-- INSERT INTO t_book_to_book_store VALUES ('Ex Libris'                , 3, 2 );
-- INSERT INTO t_book_to_book_store VALUES ('Buchhandlung im Volkshaus', 3, 1 );