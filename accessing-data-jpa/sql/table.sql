create schema jpa collate utf8mb4_general_ci;

create table jpa.device
(
    id          bigint auto_increment comment '数据id'
        primary key,
    mac         varchar(255)     not null comment 'MAC地址',
    sn          varchar(255)     not null comment '序列号',
    online      bit default b'0' not null comment '是否在线: true, false',
    active      bit default b'0' not null comment '是否激活: true, false',
    user_id     bigint           null comment '关联用户表id',
    create_time datetime         null comment '数据创建时间',
    update_time datetime         null comment '数据更新时间',
    constraint device_unique_mac_sn
        unique (mac, sn)
)
    comment '设备表';

create table jpa.user
(
    id          bigint auto_increment comment '数据id'
        primary key,
    username    varchar(255) not null comment '用户名',
    password    varchar(255) not null comment '用户密码',
    create_time datetime     null comment '数据创建时间',
    update_time datetime     null comment '数据更新时间',
    constraint user_unique_username
        unique (username)
)
    comment '用户表';

