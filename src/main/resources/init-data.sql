create table if not exists config
(
    name varchar(255) not null
        primary key,
    value varchar(255) null,
    type varchar(15) null,
    constraint config_name_uindex
        unique (name)
)
    comment '站点配置';

create table if not exists short_links
(
    `key` varchar(255) not null comment '短链接后缀'
        primary key,
    origin varchar(255) not null comment '长链接地址',
    userId int default 0 not null comment '创建者ID，游客创建则为null',
    view int default 0 not null comment '访问次数',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint short_links_key_uindex
        unique (`key`)
)
    comment '短链接存储';

create index short_links_user_id_index
    on short_links (userId);

create table if not exists users
(
    id int auto_increment
        primary key,
    name varchar(255) not null comment '用户名',
    email varchar(255) not null comment '唯一邮箱',
    password char(32) not null comment 'MD5加密，全大写',
    role int(4) default 0 not null,
    description varchar(255) null,
    registerIP varchar(255) null comment '注册ip',
    createTime timestamp default CURRENT_TIMESTAMP not null,
    loginTime timestamp null comment '登录时间',
    available tinyint(1) default 0 not null,
    constraint users_email_uindex
        unique (email),
    constraint users_id_uindex
        unique (id)
)
    comment '用户信息' auto_increment = 2;

create index users_role_index
    on users (role);

INSERT INTO config (name, value, type) VALUES ('enableTouristShorten', 'false', 'boolean');
INSERT INTO config (name, value, type) VALUES ('siteName', 'Link', 'string');
INSERT INTO config (name, value, type) VALUES ('siteUrl', 'http://localhost:8080/', 'string');

