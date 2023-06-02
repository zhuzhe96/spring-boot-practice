drop table if exists `people`;
create table people
(
    id    bigint auto_increment
        primary key,
    name  varchar(255) null,
    email varchar(255) null
);
insert into people (id, name, email)
values (1, 'Jone', 'test1@zhuzhe.com'),
       (2, 'Jack', 'test2@zhuzhe.com'),
       (3, 'Tom', 'test3@zhuzhe.com'),
       (4, 'Sandy', 'test4@zhuzhe.com'),
       (5, 'Billie', 'test5@zhuzhe.com');