DROP TABLE IF EXISTS MOVIE;

-- Create Table: MOVIE
-- ------------------------------------------------------------------------------
CREATE TABLE MOVIE
(
  `ID`            VARCHAR(36)  NOT NULL
  ,
  PRIMARY KEY (ID)
  ,
  `NAME`          VARCHAR(100) NOT NULL
  ,
  `DIRECTOR_NAME` VARCHAR(100) NOT NULL
  ,
  `GENRE`         VARCHAR(100) NOT NULL
  ,
  `RATING_ID`        VARCHAR(36)
  ,
  `ADDED_ON`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
  ENGINE = INNODB;

INSERT INTO `MOVIE` (`ID`, `NAME`, `DIRECTOR_NAME`, `GENRE`, `RATING_ID`) VALUES
  ('94d2b82b-d354-46c6-8233-c9ca3f1c9e47', 'Godfather', 'Francis Ford Coppola', 'Crime,Drama',
   '89939413-efbc-4c9e-a7e5-96920a21f47c');
INSERT INTO `MOVIE` (`ID`, `NAME`, `DIRECTOR_NAME`, `GENRE`, `RATING_ID`) VALUES
  ('8daca25d-e728-4883-977a-e26f707f3209', 'Sherlock Holmes', 'Guy Ritchie', 'Suspense,Crime',
   'e493bc00-4af8-4c13-94c0-8eb60e3187b4');
INSERT INTO `MOVIE` (`ID`, `NAME`, `DIRECTOR_NAME`, `GENRE`, `RATING_ID`) VALUES
  ('92919fc4-e287-4f4a-83d3-0610a71f591f', 'DevD', 'Anurag Kashyap', 'Lust,Drugs,Drama',
   '1c79657b-78fa-42f1-af7f-09d5b67e5cf7');
