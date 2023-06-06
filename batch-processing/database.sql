-- people表格
drop table if exists `people`;
create table people
(
    `id`    bigint not null auto_increment,
    `name`  varchar(255) null,
    `email` varchar(255) null,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='用户表' collate = utf8mb4_bin;
insert into people (name, email)
values ('Jone', 'test1@zhuzhe.com'),
       ('Jack', 'test2@zhuzhe.com'),
       ('Tom', 'test3@zhuzhe.com'),
       ('Sandy', 'test4@zhuzhe.com'),
       ('Billie', 'test5@zhuzhe.com');

-- permission表格
drop table if exists `perm`;
create table perm
(
    id   bigint not null auto_increment,
    `name` varchar(255) charset utf8mb4 null,
    pid  bigint                       null,
    url  varchar(255) charset utf8mb4 not null,
    `code` varchar(255) charset utf8mb4 not null,
    create_time datetime default CURRENT_TIMESTAMP,
    update_time datetime default CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='权限表' collate = utf8mb4_bin;
insert into perm (name, pid, url, code)
values ('菜单', 0, 'menu','menu'),
       ('页面1', 1, 'menu:page1','page'),
       ('页面2', 1, 'menu:page2','page'),
       ('权限1', 2, 'menu:page1:p1','permission'),
       ('权限2', 2, 'menu:page1:p2','permission');