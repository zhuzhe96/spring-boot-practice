drop table if exists user;
create table user
(
    id    bigint not null auto_increment,
    username  varchar(255) null,
    password varchar(255) null,
    PRIMARY KEY (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='用户表' collate = utf8mb4_bin;

create table user
(
    id         bigint auto_increment
        primary key,
    name       varchar(255) not null,
    manager_id bigint       not null
);