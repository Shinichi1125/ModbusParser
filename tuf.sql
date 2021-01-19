create database tuf

use tuf;

CREATE TABLE register (
	id int NOT NULL AUTO_INCREMENT,
    reg21 int,
    reg22 int,
    reg92 int,
    PRIMARY KEY(id)
);

INSERT INTO register VALUES (NULL, 65480, 65535, 806);
INSERT INTO register VALUES (NULL, 65470, 65535, 796);
INSERT INTO register VALUES (NULL, 65460, 65535, 786);
INSERT INTO register VALUES (NULL, 65475, 65535, 801);
INSERT INTO register VALUES (NULL, 65490, 65535, 816);

SELECT * FROM register;

DROP TABLE register;


