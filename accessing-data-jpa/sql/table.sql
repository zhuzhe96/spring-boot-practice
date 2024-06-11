create table test_db.device
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    mac         varchar(255)     null,
    sn          varchar(255)     null,
    online      bit default b'0' not null comment '在线状态',
    active      bit default b'0' not null comment '激活状态',
    user_id     bigint           null comment '绑定用户id',
    version     int              null comment '版本号',
    create_time datetime         not null comment '数据创建时间',
    update_time datetime         not null comment '数据更新时间'
)
    comment '设备表';

create table test_db.user
(
    id          bigint auto_increment
        primary key,
    username    varchar(255)  not null,
    password    varchar(255)  null,
    nick_name   varchar(255)  null,
    create_time datetime      null,
    update_time datetime      null,
    login_time  datetime      null,
    manager_id  bigint        null,
    status      int default 0 not null
);

