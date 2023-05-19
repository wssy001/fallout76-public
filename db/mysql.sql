CREATE DATABASE IF NOT EXISTS `fallout76-wiki` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ENCRYPTION = 'N';

USE `fallout76-wiki`;

create table if not exists effect
(
    id               bigint unsigned not null comment 'id'
        primary key,
    name             varchar(30)     not null comment '名称',
    value            varchar(30)     null comment '值',
    duration_seconds bigint          null comment '持续时间（秒）'
)
    comment '效果表';

create table if not exists food
(
    id              bigint unsigned               not null comment 'id'
        primary key,
    name            varchar(30)                   not null comment '中文名',
    english_name    varchar(50)                   not null comment '英文名',
    disease_chance  int(3) unsigned zerofill      not null comment '疾病概率',
    weight          float(4, 2) unsigned zerofill not null comment '重量',
    rads            int                           not null comment '辐射',
    food            int                           not null comment '食物',
    health_restored int unsigned default '0'      not null comment '生命回复',
    value           int unsigned                  not null comment '价值',
    addiction       tinyint(1)                    not null comment '是否成瘾',
    plantable       tinyint(1)                    not null comment '是否可种植'
)
    comment '食物表';

create index english_name
    on food (english_name);

create index name
    on food (name);

create table if not exists food_effect
(
    food_id   bigint unsigned not null comment '食物表id',
    effect_id bigint unsigned not null comment '效果表id'
);

create index effect_id
    on food_effect (effect_id);

create index food_id
    on food_effect (food_id);
