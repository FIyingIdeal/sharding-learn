-- 新建数据库db_user;
create database db_user;
-- 在db_user库中新建user表
create table db_user.user
(
    id   int AUTO_INCREMENT PRIMARY KEY,
    name varchar(50)
) engine = innodb;
-- 新建数据库db_account;
create database db_account;
-- 在db_account库中新建account表
create table db_account.account
(
    user_id int,
    money   double
) engine = innodb;