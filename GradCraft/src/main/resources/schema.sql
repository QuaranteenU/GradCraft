CREATE TABLE IF NOT EXISTS ceremonies
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    startTime DATETIME NOT NULL,
    name      TEXT     NOT NULL
) ENGINE = InnoDB CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS universities
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(256) NOT NULL
) ENGINE = InnoDB CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS graduates
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    name          TEXT NOT NULL,
    email         TEXT NOT NULL,
    pronunciation TEXT,
    degreeLevel   TEXT NOT NULL,
    honors        TEXT,
    major         TEXT NOT NULL,
    seniorQuote   TEXT,
    university    INT  NOT NULL,
    ceremony      INT  NOT NULL,
    uuid          VARCHAR(38),
    isHighSchool  BOOL NOT NULL DEFAULT FALSE,
    graduated     BOOL NOT NULL DEFAULT FALSE,
    timeslot      TIMESTAMP,
    FOREIGN KEY fk_university (university) REFERENCES universities (id),
    FOREIGN KEY fk_ceremony (ceremony) REFERENCES ceremonies (id)
) ENGINE = InnoDB CHARACTER SET utf8mb4;