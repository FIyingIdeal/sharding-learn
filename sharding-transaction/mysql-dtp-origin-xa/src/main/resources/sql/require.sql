create database test;
create database test1;

use test;
CREATE TABLE `user`
(
    `id`    int(11) NOT NULL AUTO_INCREMENT,
    `name`  varchar(50)  DEFAULT NULL,
    `age`   int(11)      DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


use test1;
CREATE TABLE `user`
(
    `id`    int(11) NOT NULL AUTO_INCREMENT,
    `name`  varchar(50)  DEFAULT NULL,
    `age`   int(11)      DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;