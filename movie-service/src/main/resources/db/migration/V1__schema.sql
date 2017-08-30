drop table if exists MOVIE;


-- Create Table: MOVIE
-- ------------------------------------------------------------------------------
CREATE TABLE MOVIE
(
	`ID` VARCHAR(36) NOT NULL
	,PRIMARY KEY (ID)
	,`NAME` VARCHAR(100) NOT NULL
	,`DIRECTOR_NAME` VARCHAR(100) NOT NULL
	,`GENRE` VARCHAR(100) NOT NULL
	,`ADDED_ON` TIMESTAMP DEFAULT CURRENT_TIMESTAMP	
)
ENGINE=INNODB;

INSERT INTO `MOVIE` (`ID`,`NAME`,`DIRECTOR_NAME`,`GENRE`) VALUES ('94d2b82b-d354-46c6-8233-c9ca3f1c9e47','Godfather','Francis Ford Coppola','Crime,Drama');
INSERT INTO `MOVIE` (`ID`,`NAME`,`DIRECTOR_NAME`,`GENRE`) VALUES ('8daca25d-e728-4883-977a-e26f707f3209','Sherlock Holmes','Guy Ritchie','Suspense,Crime');
INSERT INTO `MOVIE` (`ID`,`NAME`,`DIRECTOR_NAME`,`GENRE`) VALUES ('92919fc4-e287-4f4a-83d3-0610a71f591f','DevD','Anurag Kashyap','Lust,Drugs,Drama');
