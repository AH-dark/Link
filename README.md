# AHdark/Link

公私有一体的短链接系统

## SQL

```sql
create table if not exists config
(
    name  varchar(255) not null
        primary key,
    value varchar(255) null,
    type  varchar(15)  null,
    constraint config_name_uindex
        unique (name)
)
    comment '站点配置';

create table if not exists short_links
(
    `key`       varchar(255)                        not null comment '短链接后缀'
        primary key,
    origin      varchar(255)                        not null comment '长链接地址',
    userId      int       default 0                 not null comment '创建者ID，游客创建则为null',
    view        int       default 0                 not null comment '访问次数',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint short_links_key_uindex
        unique (`key`)
)
    comment '短链接存储';

create index short_links_user_id_index
    on short_links (userId);

create table if not exists users
(
    id          int auto_increment
        primary key,
    name        varchar(255)                         not null comment '用户名',
    email       varchar(255)                         not null comment '唯一邮箱',
    password    char(32)                             not null comment 'MD5加密，全大写',
    create_time timestamp  default CURRENT_TIMESTAMP not null,
    register_ip varchar(255)                         null comment '注册ip',
    login_time  timestamp                            null comment '登录时间',
    available   tinyint(1) default 0                 not null,
    constraint users_email_uindex
        unique (email),
    constraint users_id_uindex
        unique (id)
)
    comment '用户信息' auto_increment = 2;

```

> Updated on 2022.04.23

## 部署

### Java 部署

#### SQL

需要 MySQL 5.7 及以上

请预先执行建表语句（如上）

#### Program

需要预安装以下内容

- jdk 11 及以上
- maven
- npm
- git

```bash
git clone https://github.com/AH-dark/Link
cd Link
git submodule sync
cd assets/
npm install -g yarn
yarn install
yarn build
cd ../
cp assets/build/* src/main/resources/public/
cp src/main/resources/application.example.properties src/main/resources/application.properties
mvn clean
mvn package
```

此后你可以在 `target/` 目录下找到打包好的jar文件

复制jar文件到其他目录，然后继续操作

在jar程序同目录下写入配置文件 `application.properties`

```properties
# Main
server.port=8080
spring.application.name=Link Service
# Data Source
# 数据库地址、数据库名称
spring.datasource.url=jdbc:mysql://localhost:3306/link?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false
# 数据库用户名
spring.datasource.username=root
# 数据库密码
spring.datasource.password=root
# Config
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.initialization-mode=always
server.servlet.context-path=/
server.tomcat.uri-encoding=UTF-8
server.compression.enabled=true
server.servlet.session.timeout=2h
mybatis.mapper-locations=classpath*:mapper/*Mapper.xml
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=LEGACYHTML5
mybatis.configuration.map-underscore-to-camel-case=true
logging.level.com.ahdark.code.link=debug
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
spring.datasource.hikari.connection-timeout=10000
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.maximum-pool-size=6
spring.datasource.hikari.idle-timeout=40000
spring.datasource.hikari.max-lifetime=31536000001
spring.datasource.hikari.keepalive-time=31536000000
```

随后通过java启动

```bash
java -jar Link-beta.1.1.jar -Xms512m -Xmx512m
```
