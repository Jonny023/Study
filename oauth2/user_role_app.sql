create table oauth_client_details
(
    client_id               varchar(50) not null
        constraint oauth_client_details_pk
            primary key,
    resource_ids            varchar,
    client_secret           varchar     not null,
    scope                   varchar     not null,
    authorized_grant_types  varchar     not null,
    web_server_redirect_uri varchar,
    authorities             varchar,
    access_token_validity   smallint    not null,
    refresh_token_validity  smallint    not null,
    additional_information  varchar(4096),
    autoapprove             varchar(2)
);

comment on table oauth_client_details is '客户端信息';

comment on column oauth_client_details.client_id is '客户端id';

comment on column oauth_client_details.resource_ids is '可选，资源id集合，多个资源用英文逗号隔开';

comment on column oauth_client_details.client_secret is '秘钥';

comment on column oauth_client_details.scope is '权限范围，比如 read，write等可自定义';

comment on column oauth_client_details.authorized_grant_types is '授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开';

comment on column oauth_client_details.web_server_redirect_uri is '客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的';

comment on column oauth_client_details.authorities is '权限值：如ROLE_USER';

comment on column oauth_client_details.access_token_validity is 'access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时';

comment on column oauth_client_details.refresh_token_validity is 'refresh_token的有效时间值(单位:秒)不填写框架(类refreshTokenValiditySeconds)默认30天';

comment on column oauth_client_details.additional_information is '预留字段，格式必须是json';

comment on column oauth_client_details.autoapprove is '该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作';

alter table oauth_client_details
    owner to buriedpoint;

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9e314b8e61714529a8996c13c9b92118', 'buried-point-rest-service', '$2a$10$PNhtUlRZKDiDwX9emwHDDeLLqvFkUTk9sBSY7RK9VxumtY5U7tv5O', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9aff8ab069184f5390bcbe055025c84b', 'buried-point-rest-service', '$2a$10$f0Xvm59QFUglbw4enmPYkO47YV5mLENNP/3sUrSSt24enJKil/eT.', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('406739f89cf74198a58c3c0359dace7a', 'buried-point-rest-service', '$2a$10$dbrDMlLwV10HkC2/BUSDTuLthuoqqdsJGVFlA57b7CXV6.jBaXBae', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');create table sys_app
(
    id           bigserial    not null,
    app_id       varchar(255),
    app_secret   varchar(255),
    bid          varchar(255),
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    name         varchar(100) not null,
    description  varchar(255),
    create_user  varchar(32)  not null,
    update_user  varchar(32),
    logic_delete boolean   default false
);

comment on table sys_app is '应用';

comment on column sys_app.id is '主键ID';

comment on column sys_app.app_id is '应用标识';

comment on column sys_app.app_secret is '应用秘钥(明文)';

comment on column sys_app.bid is '业务id';

comment on column sys_app.create_time is '创建时间';

comment on column sys_app.update_time is '更新时间';

comment on column sys_app.name is '应用名称';

comment on column sys_app.description is '描述';

comment on column sys_app.create_user is '创建人';

comment on column sys_app.update_user is '更新人';

comment on column sys_app.logic_delete is '是否删除（false-否,true-是）';

alter table sys_app
    owner to buriedpoint;

INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (1, '9e314b8e61714529a8996c13c9b92118', '0e9d129a4cea481b8c0db3ba32791842', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, '', null, 'aaa', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (2, '9aff8ab069184f5390bcbe055025c84b', 'bac396d5f2444f35bdd0753de218a46b', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, '', null, 'admin', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (3, '406739f89cf74198a58c3c0359dace7a', '73654353655149f29fd41bc7818576c9', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, '', null, '123', null, false);create table sys_permission
(
    id           bigserial                                  not null
        constraint sys_permission_pkey
            primary key,
    parent_bid   varchar(32)  default ''::character varying not null,
    name         varchar(64)                                not null,
    enname       varchar(64)                                not null,
    url          varchar(255)                               not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now()                 not null,
    update_time  timestamp    default now()                 not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)                                not null
);

comment on table sys_permission is '权限表';

comment on column sys_permission.id is '主键';

comment on column sys_permission.parent_bid is '父业务id';

comment on column sys_permission.name is '权限名称';

comment on column sys_permission.enname is '权限英文名称';

comment on column sys_permission.url is '授权路径';

comment on column sys_permission.description is '备注';

comment on column sys_permission.create_time is '创建时间';

comment on column sys_permission.update_time is '更新时间';

comment on column sys_permission.create_user is '创建人';

comment on column sys_permission.update_user is '更新人';

comment on column sys_permission.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_permission.bid is '业务id';

alter table sys_permission
    owner to buriedpoint;

INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzzneg', 'dd', '这是新增的权限', '2021-04-21 13:55:24.931000', '2021-04-21 13:55:24.931000', '123', '123', false, '99bb710df8704791a41b2ac06c909e4d');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegee', 'dd', '这是新增的权限', '2021-04-21 13:58:28.352000', '2021-04-21 13:58:28.352000', '123', '123', false, '09c9d7c2e8854f72932b2bcc9a093f04');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeee', 'dd', '这是新增的权限', '2021-04-21 13:59:06.876000', '2021-04-21 14:16:04.383000', '123', '123', false, '4e01101092134778a0352628e975ee58');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeeeee', '收到', '这是新增的权限', '2021-04-21 14:16:29.093000', '2021-04-21 14:17:20.331000', '123', '123', true, '745a690f84334f79af9e09ecac86cdd5');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '', 'asdf112', 'abc', '/ab', '', '2021-04-20 17:27:42.115569', '2021-04-21 15:48:28.997000', 'admin', 'admin', false, 'fbaaeca6b3694a1e81bfef28241a64cf');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '', '用户管理', 'abc_user', '/sfa', '', '2021-04-22 14:50:54.824000', '2021-04-22 15:07:11.643000', 'admin', 'admin', false, '4a33db1dac8c429fa796181ff9dfb8b4');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '', '', '/t', '', '2021-04-22 17:31:26.310000', '2021-04-22 17:36:16.134000', '123', '123', false, '0464c5376b814a55a37cb44c8de46ed1');create table sys_role
(
    id           bigserial                  not null
        constraint sys_role_pkey
            primary key,
    parent_bid   varchar(32)  default 0,
    name         varchar(64)                not null,
    enname       varchar(64)                not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now() not null,
    update_time  timestamp    default now() not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键';

comment on column sys_role.parent_bid is '父业务id';

comment on column sys_role.name is '角色名称';

comment on column sys_role.enname is '角色英文名称';

comment on column sys_role.description is '备注';

comment on column sys_role.create_time is '创建时间';

comment on column sys_role.update_time is '更新时间';

comment on column sys_role.create_user is '创建人';

comment on column sys_role.update_user is '更新人';

comment on column sys_role.logic_delete is '是否删除（false-否,true-是）';

comment on column sys_role.bid is '业务id';

alter table sys_role
    owner to buriedpoint;

INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, '', '普通用户', 'liwen', '这是新增角色李文', '2021-04-21 12:55:11.020000', '2021-04-21 12:55:11.020000', '123', '123', false, '48f9c475700f45bc9fc536dfa1ef78c4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen1', '这是新增角色李文', '2021-04-21 12:56:30.457000', '2021-04-21 12:56:30.457000', '123', '123', false, 'b55679d09af849aaa233168c57cd5cda');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen2', '这是新增角色李文', '2021-04-21 13:04:58.874000', '2021-04-21 13:04:58.874000', '123', '123', false, '487c454b40cc475bb5be03b93d0001e4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '超级用户', 'zhangdg', '这是新增角色李文11111', '2021-04-21 13:33:37.736000', '2021-04-21 13:33:37.736000', '123', '123', false, '4e1c02e7d6fc4e2aacd468e759d49dce');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (8, '', '测试角色112', 'USER112', 'abc', '2021-04-21 13:37:56.600000', '2021-04-21 13:37:56.600000', 'admin', 'admin', false, '52f2f0bf64954bb5afebf681d105090e');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'USER', null, '2021-04-20 17:09:20.002839', '2021-04-20 17:09:20.002839', 'zz', null, false, 'b3379836bd0e4f4a8b233c4b4b2391dc');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, '48f9c475700f45bc9fc536dfa1ef78c4', '用户1', 'USER1', null, '2021-04-20 17:23:34.293602', '2021-04-20 17:23:34.293602', null, null, false, 'b3379836bd0e4f4a8b233c4b4b2391d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (10, '', '角色121', 'USER121', 'abc', '2021-04-21 13:51:33.516000', '2021-04-21 13:51:33.516000', 'admin', 'admin', true, '6f9e5aa78a1c4c57940a538f9a3c3d58');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (11, '1', '1', '1', '1', '2021-04-21 14:04:27.810000', '2021-04-21 14:04:27.810000', '123', '123', true, 'b001fd724daf4fe2bf5c32528c5c24d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (12, '', '1', '1', '', '2021-04-22 15:28:06.922000', '2021-04-22 15:28:06.922000', 'admin', 'admin', true, '12d32de42eb04b25b8e50b7185a92bc8');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (13, '', '1', '1', '', '2021-04-22 15:28:34.373000', '2021-04-22 15:28:34.373000', 'admin', 'admin', false, '2e1151600a2345f3a10b6a6b7cf86087');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen3', '这是新增角色李文888', '2021-04-21 13:12:48.444000', '2021-04-22 17:40:11.409000', '123', '123', false, '1bb9cf6be1de4fe1bb1cbed84d535955');create table sys_role_permission
(
    id             bigserial               not null
        constraint sys_role_permission_pkey
            primary key,
    role_bid       varchar(32)             not null,
    permission_bid varchar(32)             not null,
    create_time    timestamp default now() not null,
    update_time    timestamp default now(),
    create_user    varchar(50),
    update_user    varchar(50),
    logic_delete   boolean   default false
);

comment on table sys_role_permission is '角色权限表';

comment on column sys_role_permission.id is '主键';

comment on column sys_role_permission.role_bid is '角色业务id';

comment on column sys_role_permission.permission_bid is '权限业务id';

comment on column sys_role_permission.create_time is '创建时间';

comment on column sys_role_permission.update_time is '更新时间';

comment on column sys_role_permission.create_user is '创建人';

comment on column sys_role_permission.update_user is '更新人';

comment on column sys_role_permission.logic_delete is '是否删除（0-否,1-是）';

alter table sys_role_permission
    owner to buriedpoint;

INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (1, 'b3379836bd0e4f4a8b233c4b4b2391d1', 'b13379836bd0e4f4a8b233c4b4b2391d', '2021-04-20 17:27:54.016000', '2021-04-20 17:45:51.489000', 'admin', 'admin', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (15, '1', '1', '2021-04-21 13:49:58.665000', '2021-04-21 13:49:58.665000', '123', '123', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (16, '2', '2', '2021-04-21 13:52:31.455000', '2021-04-21 13:52:31.455000', '123', '123', false);create table sys_user
(
    id           bigserial               not null
        constraint sys_user_pk
            primary key,
    username     varchar(30)             not null
        constraint sys_user_un_username
            unique,
    password     varchar(100)            not null,
    nickname     varchar(40),
    phone        varchar(20)
        constraint sys_user_un_phone
            unique,
    email        varchar(50)
        constraint sys_user_un_email
            unique,
    create_time  timestamp default now() not null,
    update_time  timestamp default now() not null,
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    bid          varchar(32)             not null
);

comment on table sys_user is '用户信息';

comment on column sys_user.id is '主键';

comment on column sys_user.username is '用户名';

comment on column sys_user.password is '密码';

comment on column sys_user.nickname is '昵称';

comment on column sys_user.phone is '手机号';

comment on column sys_user.email is '邮箱';

comment on column sys_user.create_time is '创建时间';

comment on column sys_user.update_time is '更新时间';

comment on column sys_user.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user.create_user is '创建人';

comment on column sys_user.update_user is '更新人';

comment on column sys_user.bid is '业务id';

alter table sys_user
    owner to buriedpoint;

create index sys_user_email_idx
    on sys_user (email);

create index sys_user_phone_idx
    on sys_user (phone);

create index sys_user_username_idx
    on sys_user (username);

INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (4, 'aaa', '$2a$10$8yEHHH5G0GS4cW07wCu3SuDlhK9jPO90.Lp2pdeRb2UeZovZWRQEW', 'laohu', '18883330333', '539@geely.com', '2021-04-20 17:15:21.656361', '2021-04-20 17:15:21.594000', false, null, null, '259aad25f5f84212b95e67a80b3574c2');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (5, 'admin', '$2a$10$sHuitqfBFbIqYyvjAHYLy.uASY1kwSlQNmjEWrNzJaWcxvGLICFE6', '管理员', '13210101010', '123@qq.com', '2021-04-20 17:17:37.208353', '2021-04-20 17:17:37.070000', false, null, null, '3597087cb92040a2b084e1577f6fbaef');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (6, '123', '$2a$10$susmISxDIlZCJAJVS45hAOlSwXkLpYbdP8MhZYEIZ.pmmV5tMLnw2', '11', '13512367543', '781203456@qq.com', '2021-04-21 11:37:53.072818', '2021-04-21 11:37:52.983000', false, null, null, '1b6325eb2cbc4ccf9952ff21254a3698');create table sys_user_app
(
    id           bigserial   not null
        constraint sys_user_app_pkey
            primary key,
    user_bid     varchar(32) not null,
    app_bid      varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    constraint sys_user_app_un
        unique (user_bid, app_bid)
);

comment on table sys_user_app is '用户app表';

comment on column sys_user_app.id is '主键';

comment on column sys_user_app.user_bid is '用户业务id';

comment on column sys_user_app.app_bid is '应用业务id';

comment on column sys_user_app.create_time is '创建时间';

comment on column sys_user_app.update_time is '更新时间';

comment on column sys_user_app.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_app.create_user is '创建人';

comment on column sys_user_app.update_user is '更新人';

alter table sys_user_app
    owner to buriedpoint;

INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, false, '123', null);create table sys_user_role
(
    id           bigserial   not null
        constraint sys_user_role_pkey
            primary key,
    user_bid     varchar(32) not null,
    role_bid     varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(50),
    update_user  varchar(50),
    constraint sys_user_role_un
        unique (user_bid, role_bid)
);

comment on table sys_user_role is '用户角色表';

comment on column sys_user_role.id is '主键';

comment on column sys_user_role.user_bid is '用户业务id';

comment on column sys_user_role.role_bid is '角色业务id';

comment on column sys_user_role.create_time is '创建时间';

comment on column sys_user_role.update_time is '更新时间';

comment on column sys_user_role.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_role.create_user is '创建人';

comment on column sys_user_role.update_user is '更新人';

alter table sys_user_role
    owner to buriedpoint;

INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-21 11:37:53.072818', null, false, '123', null);create table oauth_client_details
(
    client_id               varchar(50) not null
        constraint oauth_client_details_pk
            primary key,
    resource_ids            varchar,
    client_secret           varchar     not null,
    scope                   varchar     not null,
    authorized_grant_types  varchar     not null,
    web_server_redirect_uri varchar,
    authorities             varchar,
    access_token_validity   smallint    not null,
    refresh_token_validity  smallint    not null,
    additional_information  varchar(4096),
    autoapprove             varchar(2)
);

comment on table oauth_client_details is '客户端信息';

comment on column oauth_client_details.client_id is '客户端id';

comment on column oauth_client_details.resource_ids is '可选，资源id集合，多个资源用英文逗号隔开';

comment on column oauth_client_details.client_secret is '秘钥';

comment on column oauth_client_details.scope is '权限范围，比如 read，write等可自定义';

comment on column oauth_client_details.authorized_grant_types is '授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开';

comment on column oauth_client_details.web_server_redirect_uri is '客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的';

comment on column oauth_client_details.authorities is '权限值：如ROLE_USER';

comment on column oauth_client_details.access_token_validity is 'access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时';

comment on column oauth_client_details.refresh_token_validity is 'refresh_token的有效时间值(单位:秒)不填写框架(类refreshTokenValiditySeconds)默认30天';

comment on column oauth_client_details.additional_information is '预留字段，格式必须是json';

comment on column oauth_client_details.autoapprove is '该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作';

alter table oauth_client_details
    owner to buriedpoint;

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9e314b8e61714529a8996c13c9b92118', 'buried-point-rest-service', '$2a$10$PNhtUlRZKDiDwX9emwHDDeLLqvFkUTk9sBSY7RK9VxumtY5U7tv5O', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9aff8ab069184f5390bcbe055025c84b', 'buried-point-rest-service', '$2a$10$f0Xvm59QFUglbw4enmPYkO47YV5mLENNP/3sUrSSt24enJKil/eT.', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('406739f89cf74198a58c3c0359dace7a', 'buried-point-rest-service', '$2a$10$dbrDMlLwV10HkC2/BUSDTuLthuoqqdsJGVFlA57b7CXV6.jBaXBae', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');create table sys_app
(
    id           bigserial    not null,
    app_id       varchar(255),
    app_secret   varchar(255),
    bid          varchar(255),
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    name         varchar(100) not null,
    description  varchar(255),
    create_user  varchar(32)  not null,
    update_user  varchar(32),
    logic_delete boolean   default false
);

comment on table sys_app is '应用';

comment on column sys_app.id is '主键ID';

comment on column sys_app.app_id is '应用标识';

comment on column sys_app.app_secret is '应用秘钥(明文)';

comment on column sys_app.bid is '业务id';

comment on column sys_app.create_time is '创建时间';

comment on column sys_app.update_time is '更新时间';

comment on column sys_app.name is '应用名称';

comment on column sys_app.description is '描述';

comment on column sys_app.create_user is '创建人';

comment on column sys_app.update_user is '更新人';

comment on column sys_app.logic_delete is '是否删除（false-否,true-是）';

alter table sys_app
    owner to buriedpoint;

INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (1, '9e314b8e61714529a8996c13c9b92118', '0e9d129a4cea481b8c0db3ba32791842', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, '', null, 'aaa', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (2, '9aff8ab069184f5390bcbe055025c84b', 'bac396d5f2444f35bdd0753de218a46b', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, '', null, 'admin', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (3, '406739f89cf74198a58c3c0359dace7a', '73654353655149f29fd41bc7818576c9', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, '', null, '123', null, false);create table sys_permission
(
    id           bigserial                                  not null
        constraint sys_permission_pkey
            primary key,
    parent_bid   varchar(32)  default ''::character varying not null,
    name         varchar(64)                                not null,
    enname       varchar(64)                                not null,
    url          varchar(255)                               not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now()                 not null,
    update_time  timestamp    default now()                 not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)                                not null
);

comment on table sys_permission is '权限表';

comment on column sys_permission.id is '主键';

comment on column sys_permission.parent_bid is '父业务id';

comment on column sys_permission.name is '权限名称';

comment on column sys_permission.enname is '权限英文名称';

comment on column sys_permission.url is '授权路径';

comment on column sys_permission.description is '备注';

comment on column sys_permission.create_time is '创建时间';

comment on column sys_permission.update_time is '更新时间';

comment on column sys_permission.create_user is '创建人';

comment on column sys_permission.update_user is '更新人';

comment on column sys_permission.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_permission.bid is '业务id';

alter table sys_permission
    owner to buriedpoint;

INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzzneg', 'dd', '这是新增的权限', '2021-04-21 13:55:24.931000', '2021-04-21 13:55:24.931000', '123', '123', false, '99bb710df8704791a41b2ac06c909e4d');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegee', 'dd', '这是新增的权限', '2021-04-21 13:58:28.352000', '2021-04-21 13:58:28.352000', '123', '123', false, '09c9d7c2e8854f72932b2bcc9a093f04');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeee', 'dd', '这是新增的权限', '2021-04-21 13:59:06.876000', '2021-04-21 14:16:04.383000', '123', '123', false, '4e01101092134778a0352628e975ee58');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeeeee', '收到', '这是新增的权限', '2021-04-21 14:16:29.093000', '2021-04-21 14:17:20.331000', '123', '123', true, '745a690f84334f79af9e09ecac86cdd5');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '', 'asdf112', 'abc', '/ab', '', '2021-04-20 17:27:42.115569', '2021-04-21 15:48:28.997000', 'admin', 'admin', false, 'fbaaeca6b3694a1e81bfef28241a64cf');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '', '用户管理', 'abc_user', '/sfa', '', '2021-04-22 14:50:54.824000', '2021-04-22 15:07:11.643000', 'admin', 'admin', false, '4a33db1dac8c429fa796181ff9dfb8b4');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '', '', '/t', '', '2021-04-22 17:31:26.310000', '2021-04-22 17:36:16.134000', '123', '123', false, '0464c5376b814a55a37cb44c8de46ed1');create table sys_role
(
    id           bigserial                  not null
        constraint sys_role_pkey
            primary key,
    parent_bid   varchar(32)  default 0,
    name         varchar(64)                not null,
    enname       varchar(64)                not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now() not null,
    update_time  timestamp    default now() not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键';

comment on column sys_role.parent_bid is '父业务id';

comment on column sys_role.name is '角色名称';

comment on column sys_role.enname is '角色英文名称';

comment on column sys_role.description is '备注';

comment on column sys_role.create_time is '创建时间';

comment on column sys_role.update_time is '更新时间';

comment on column sys_role.create_user is '创建人';

comment on column sys_role.update_user is '更新人';

comment on column sys_role.logic_delete is '是否删除（false-否,true-是）';

comment on column sys_role.bid is '业务id';

alter table sys_role
    owner to buriedpoint;

INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, '', '普通用户', 'liwen', '这是新增角色李文', '2021-04-21 12:55:11.020000', '2021-04-21 12:55:11.020000', '123', '123', false, '48f9c475700f45bc9fc536dfa1ef78c4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen1', '这是新增角色李文', '2021-04-21 12:56:30.457000', '2021-04-21 12:56:30.457000', '123', '123', false, 'b55679d09af849aaa233168c57cd5cda');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen2', '这是新增角色李文', '2021-04-21 13:04:58.874000', '2021-04-21 13:04:58.874000', '123', '123', false, '487c454b40cc475bb5be03b93d0001e4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '超级用户', 'zhangdg', '这是新增角色李文11111', '2021-04-21 13:33:37.736000', '2021-04-21 13:33:37.736000', '123', '123', false, '4e1c02e7d6fc4e2aacd468e759d49dce');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (8, '', '测试角色112', 'USER112', 'abc', '2021-04-21 13:37:56.600000', '2021-04-21 13:37:56.600000', 'admin', 'admin', false, '52f2f0bf64954bb5afebf681d105090e');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'USER', null, '2021-04-20 17:09:20.002839', '2021-04-20 17:09:20.002839', 'zz', null, false, 'b3379836bd0e4f4a8b233c4b4b2391dc');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, '48f9c475700f45bc9fc536dfa1ef78c4', '用户1', 'USER1', null, '2021-04-20 17:23:34.293602', '2021-04-20 17:23:34.293602', null, null, false, 'b3379836bd0e4f4a8b233c4b4b2391d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (10, '', '角色121', 'USER121', 'abc', '2021-04-21 13:51:33.516000', '2021-04-21 13:51:33.516000', 'admin', 'admin', true, '6f9e5aa78a1c4c57940a538f9a3c3d58');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (11, '1', '1', '1', '1', '2021-04-21 14:04:27.810000', '2021-04-21 14:04:27.810000', '123', '123', true, 'b001fd724daf4fe2bf5c32528c5c24d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (12, '', '1', '1', '', '2021-04-22 15:28:06.922000', '2021-04-22 15:28:06.922000', 'admin', 'admin', true, '12d32de42eb04b25b8e50b7185a92bc8');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (13, '', '1', '1', '', '2021-04-22 15:28:34.373000', '2021-04-22 15:28:34.373000', 'admin', 'admin', false, '2e1151600a2345f3a10b6a6b7cf86087');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen3', '这是新增角色李文888', '2021-04-21 13:12:48.444000', '2021-04-22 17:40:11.409000', '123', '123', false, '1bb9cf6be1de4fe1bb1cbed84d535955');create table sys_role_permission
(
    id             bigserial               not null
        constraint sys_role_permission_pkey
            primary key,
    role_bid       varchar(32)             not null,
    permission_bid varchar(32)             not null,
    create_time    timestamp default now() not null,
    update_time    timestamp default now(),
    create_user    varchar(50),
    update_user    varchar(50),
    logic_delete   boolean   default false
);

comment on table sys_role_permission is '角色权限表';

comment on column sys_role_permission.id is '主键';

comment on column sys_role_permission.role_bid is '角色业务id';

comment on column sys_role_permission.permission_bid is '权限业务id';

comment on column sys_role_permission.create_time is '创建时间';

comment on column sys_role_permission.update_time is '更新时间';

comment on column sys_role_permission.create_user is '创建人';

comment on column sys_role_permission.update_user is '更新人';

comment on column sys_role_permission.logic_delete is '是否删除（0-否,1-是）';

alter table sys_role_permission
    owner to buriedpoint;

INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (1, 'b3379836bd0e4f4a8b233c4b4b2391d1', 'b13379836bd0e4f4a8b233c4b4b2391d', '2021-04-20 17:27:54.016000', '2021-04-20 17:45:51.489000', 'admin', 'admin', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (15, '1', '1', '2021-04-21 13:49:58.665000', '2021-04-21 13:49:58.665000', '123', '123', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (16, '2', '2', '2021-04-21 13:52:31.455000', '2021-04-21 13:52:31.455000', '123', '123', false);create table sys_user
(
    id           bigserial               not null
        constraint sys_user_pk
            primary key,
    username     varchar(30)             not null
        constraint sys_user_un_username
            unique,
    password     varchar(100)            not null,
    nickname     varchar(40),
    phone        varchar(20)
        constraint sys_user_un_phone
            unique,
    email        varchar(50)
        constraint sys_user_un_email
            unique,
    create_time  timestamp default now() not null,
    update_time  timestamp default now() not null,
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    bid          varchar(32)             not null
);

comment on table sys_user is '用户信息';

comment on column sys_user.id is '主键';

comment on column sys_user.username is '用户名';

comment on column sys_user.password is '密码';

comment on column sys_user.nickname is '昵称';

comment on column sys_user.phone is '手机号';

comment on column sys_user.email is '邮箱';

comment on column sys_user.create_time is '创建时间';

comment on column sys_user.update_time is '更新时间';

comment on column sys_user.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user.create_user is '创建人';

comment on column sys_user.update_user is '更新人';

comment on column sys_user.bid is '业务id';

alter table sys_user
    owner to buriedpoint;

create index sys_user_email_idx
    on sys_user (email);

create index sys_user_phone_idx
    on sys_user (phone);

create index sys_user_username_idx
    on sys_user (username);

INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (4, 'aaa', '$2a$10$8yEHHH5G0GS4cW07wCu3SuDlhK9jPO90.Lp2pdeRb2UeZovZWRQEW', 'laohu', '18883330333', '539@geely.com', '2021-04-20 17:15:21.656361', '2021-04-20 17:15:21.594000', false, null, null, '259aad25f5f84212b95e67a80b3574c2');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (5, 'admin', '$2a$10$sHuitqfBFbIqYyvjAHYLy.uASY1kwSlQNmjEWrNzJaWcxvGLICFE6', '管理员', '13210101010', '123@qq.com', '2021-04-20 17:17:37.208353', '2021-04-20 17:17:37.070000', false, null, null, '3597087cb92040a2b084e1577f6fbaef');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (6, '123', '$2a$10$susmISxDIlZCJAJVS45hAOlSwXkLpYbdP8MhZYEIZ.pmmV5tMLnw2', '11', '13512367543', '781203456@qq.com', '2021-04-21 11:37:53.072818', '2021-04-21 11:37:52.983000', false, null, null, '1b6325eb2cbc4ccf9952ff21254a3698');create table sys_user_app
(
    id           bigserial   not null
        constraint sys_user_app_pkey
            primary key,
    user_bid     varchar(32) not null,
    app_bid      varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    constraint sys_user_app_un
        unique (user_bid, app_bid)
);

comment on table sys_user_app is '用户app表';

comment on column sys_user_app.id is '主键';

comment on column sys_user_app.user_bid is '用户业务id';

comment on column sys_user_app.app_bid is '应用业务id';

comment on column sys_user_app.create_time is '创建时间';

comment on column sys_user_app.update_time is '更新时间';

comment on column sys_user_app.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_app.create_user is '创建人';

comment on column sys_user_app.update_user is '更新人';

alter table sys_user_app
    owner to buriedpoint;

INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, false, '123', null);create table sys_user_role
(
    id           bigserial   not null
        constraint sys_user_role_pkey
            primary key,
    user_bid     varchar(32) not null,
    role_bid     varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(50),
    update_user  varchar(50),
    constraint sys_user_role_un
        unique (user_bid, role_bid)
);

comment on table sys_user_role is '用户角色表';

comment on column sys_user_role.id is '主键';

comment on column sys_user_role.user_bid is '用户业务id';

comment on column sys_user_role.role_bid is '角色业务id';

comment on column sys_user_role.create_time is '创建时间';

comment on column sys_user_role.update_time is '更新时间';

comment on column sys_user_role.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_role.create_user is '创建人';

comment on column sys_user_role.update_user is '更新人';

alter table sys_user_role
    owner to buriedpoint;

INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-21 11:37:53.072818', null, false, '123', null);create table oauth_client_details
(
    client_id               varchar(50) not null
        constraint oauth_client_details_pk
            primary key,
    resource_ids            varchar,
    client_secret           varchar     not null,
    scope                   varchar     not null,
    authorized_grant_types  varchar     not null,
    web_server_redirect_uri varchar,
    authorities             varchar,
    access_token_validity   smallint    not null,
    refresh_token_validity  smallint    not null,
    additional_information  varchar(4096),
    autoapprove             varchar(2)
);

comment on table oauth_client_details is '客户端信息';

comment on column oauth_client_details.client_id is '客户端id';

comment on column oauth_client_details.resource_ids is '可选，资源id集合，多个资源用英文逗号隔开';

comment on column oauth_client_details.client_secret is '秘钥';

comment on column oauth_client_details.scope is '权限范围，比如 read，write等可自定义';

comment on column oauth_client_details.authorized_grant_types is '授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开';

comment on column oauth_client_details.web_server_redirect_uri is '客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的';

comment on column oauth_client_details.authorities is '权限值：如ROLE_USER';

comment on column oauth_client_details.access_token_validity is 'access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时';

comment on column oauth_client_details.refresh_token_validity is 'refresh_token的有效时间值(单位:秒)不填写框架(类refreshTokenValiditySeconds)默认30天';

comment on column oauth_client_details.additional_information is '预留字段，格式必须是json';

comment on column oauth_client_details.autoapprove is '该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作';

alter table oauth_client_details
    owner to buriedpoint;

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9e314b8e61714529a8996c13c9b92118', 'buried-point-rest-service', '$2a$10$PNhtUlRZKDiDwX9emwHDDeLLqvFkUTk9sBSY7RK9VxumtY5U7tv5O', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9aff8ab069184f5390bcbe055025c84b', 'buried-point-rest-service', '$2a$10$f0Xvm59QFUglbw4enmPYkO47YV5mLENNP/3sUrSSt24enJKil/eT.', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('406739f89cf74198a58c3c0359dace7a', 'buried-point-rest-service', '$2a$10$dbrDMlLwV10HkC2/BUSDTuLthuoqqdsJGVFlA57b7CXV6.jBaXBae', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');create table sys_app
(
    id           bigserial    not null,
    app_id       varchar(255),
    app_secret   varchar(255),
    bid          varchar(255),
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    name         varchar(100) not null,
    description  varchar(255),
    create_user  varchar(32)  not null,
    update_user  varchar(32),
    logic_delete boolean   default false
);

comment on table sys_app is '应用';

comment on column sys_app.id is '主键ID';

comment on column sys_app.app_id is '应用标识';

comment on column sys_app.app_secret is '应用秘钥(明文)';

comment on column sys_app.bid is '业务id';

comment on column sys_app.create_time is '创建时间';

comment on column sys_app.update_time is '更新时间';

comment on column sys_app.name is '应用名称';

comment on column sys_app.description is '描述';

comment on column sys_app.create_user is '创建人';

comment on column sys_app.update_user is '更新人';

comment on column sys_app.logic_delete is '是否删除（false-否,true-是）';

alter table sys_app
    owner to buriedpoint;

INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (1, '9e314b8e61714529a8996c13c9b92118', '0e9d129a4cea481b8c0db3ba32791842', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, '', null, 'aaa', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (2, '9aff8ab069184f5390bcbe055025c84b', 'bac396d5f2444f35bdd0753de218a46b', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, '', null, 'admin', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (3, '406739f89cf74198a58c3c0359dace7a', '73654353655149f29fd41bc7818576c9', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, '', null, '123', null, false);create table sys_permission
(
    id           bigserial                                  not null
        constraint sys_permission_pkey
            primary key,
    parent_bid   varchar(32)  default ''::character varying not null,
    name         varchar(64)                                not null,
    enname       varchar(64)                                not null,
    url          varchar(255)                               not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now()                 not null,
    update_time  timestamp    default now()                 not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)                                not null
);

comment on table sys_permission is '权限表';

comment on column sys_permission.id is '主键';

comment on column sys_permission.parent_bid is '父业务id';

comment on column sys_permission.name is '权限名称';

comment on column sys_permission.enname is '权限英文名称';

comment on column sys_permission.url is '授权路径';

comment on column sys_permission.description is '备注';

comment on column sys_permission.create_time is '创建时间';

comment on column sys_permission.update_time is '更新时间';

comment on column sys_permission.create_user is '创建人';

comment on column sys_permission.update_user is '更新人';

comment on column sys_permission.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_permission.bid is '业务id';

alter table sys_permission
    owner to buriedpoint;

INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzzneg', 'dd', '这是新增的权限', '2021-04-21 13:55:24.931000', '2021-04-21 13:55:24.931000', '123', '123', false, '99bb710df8704791a41b2ac06c909e4d');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegee', 'dd', '这是新增的权限', '2021-04-21 13:58:28.352000', '2021-04-21 13:58:28.352000', '123', '123', false, '09c9d7c2e8854f72932b2bcc9a093f04');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeee', 'dd', '这是新增的权限', '2021-04-21 13:59:06.876000', '2021-04-21 14:16:04.383000', '123', '123', false, '4e01101092134778a0352628e975ee58');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeeeee', '收到', '这是新增的权限', '2021-04-21 14:16:29.093000', '2021-04-21 14:17:20.331000', '123', '123', true, '745a690f84334f79af9e09ecac86cdd5');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '', 'asdf112', 'abc', '/ab', '', '2021-04-20 17:27:42.115569', '2021-04-21 15:48:28.997000', 'admin', 'admin', false, 'fbaaeca6b3694a1e81bfef28241a64cf');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '', '用户管理', 'abc_user', '/sfa', '', '2021-04-22 14:50:54.824000', '2021-04-22 15:07:11.643000', 'admin', 'admin', false, '4a33db1dac8c429fa796181ff9dfb8b4');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '', '', '/t', '', '2021-04-22 17:31:26.310000', '2021-04-22 17:36:16.134000', '123', '123', false, '0464c5376b814a55a37cb44c8de46ed1');create table sys_role
(
    id           bigserial                  not null
        constraint sys_role_pkey
            primary key,
    parent_bid   varchar(32)  default 0,
    name         varchar(64)                not null,
    enname       varchar(64)                not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now() not null,
    update_time  timestamp    default now() not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键';

comment on column sys_role.parent_bid is '父业务id';

comment on column sys_role.name is '角色名称';

comment on column sys_role.enname is '角色英文名称';

comment on column sys_role.description is '备注';

comment on column sys_role.create_time is '创建时间';

comment on column sys_role.update_time is '更新时间';

comment on column sys_role.create_user is '创建人';

comment on column sys_role.update_user is '更新人';

comment on column sys_role.logic_delete is '是否删除（false-否,true-是）';

comment on column sys_role.bid is '业务id';

alter table sys_role
    owner to buriedpoint;

INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, '', '普通用户', 'liwen', '这是新增角色李文', '2021-04-21 12:55:11.020000', '2021-04-21 12:55:11.020000', '123', '123', false, '48f9c475700f45bc9fc536dfa1ef78c4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen1', '这是新增角色李文', '2021-04-21 12:56:30.457000', '2021-04-21 12:56:30.457000', '123', '123', false, 'b55679d09af849aaa233168c57cd5cda');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen2', '这是新增角色李文', '2021-04-21 13:04:58.874000', '2021-04-21 13:04:58.874000', '123', '123', false, '487c454b40cc475bb5be03b93d0001e4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '超级用户', 'zhangdg', '这是新增角色李文11111', '2021-04-21 13:33:37.736000', '2021-04-21 13:33:37.736000', '123', '123', false, '4e1c02e7d6fc4e2aacd468e759d49dce');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (8, '', '测试角色112', 'USER112', 'abc', '2021-04-21 13:37:56.600000', '2021-04-21 13:37:56.600000', 'admin', 'admin', false, '52f2f0bf64954bb5afebf681d105090e');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'USER', null, '2021-04-20 17:09:20.002839', '2021-04-20 17:09:20.002839', 'zz', null, false, 'b3379836bd0e4f4a8b233c4b4b2391dc');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, '48f9c475700f45bc9fc536dfa1ef78c4', '用户1', 'USER1', null, '2021-04-20 17:23:34.293602', '2021-04-20 17:23:34.293602', null, null, false, 'b3379836bd0e4f4a8b233c4b4b2391d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (10, '', '角色121', 'USER121', 'abc', '2021-04-21 13:51:33.516000', '2021-04-21 13:51:33.516000', 'admin', 'admin', true, '6f9e5aa78a1c4c57940a538f9a3c3d58');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (11, '1', '1', '1', '1', '2021-04-21 14:04:27.810000', '2021-04-21 14:04:27.810000', '123', '123', true, 'b001fd724daf4fe2bf5c32528c5c24d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (12, '', '1', '1', '', '2021-04-22 15:28:06.922000', '2021-04-22 15:28:06.922000', 'admin', 'admin', true, '12d32de42eb04b25b8e50b7185a92bc8');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (13, '', '1', '1', '', '2021-04-22 15:28:34.373000', '2021-04-22 15:28:34.373000', 'admin', 'admin', false, '2e1151600a2345f3a10b6a6b7cf86087');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen3', '这是新增角色李文888', '2021-04-21 13:12:48.444000', '2021-04-22 17:40:11.409000', '123', '123', false, '1bb9cf6be1de4fe1bb1cbed84d535955');create table sys_role_permission
(
    id             bigserial               not null
        constraint sys_role_permission_pkey
            primary key,
    role_bid       varchar(32)             not null,
    permission_bid varchar(32)             not null,
    create_time    timestamp default now() not null,
    update_time    timestamp default now(),
    create_user    varchar(50),
    update_user    varchar(50),
    logic_delete   boolean   default false
);

comment on table sys_role_permission is '角色权限表';

comment on column sys_role_permission.id is '主键';

comment on column sys_role_permission.role_bid is '角色业务id';

comment on column sys_role_permission.permission_bid is '权限业务id';

comment on column sys_role_permission.create_time is '创建时间';

comment on column sys_role_permission.update_time is '更新时间';

comment on column sys_role_permission.create_user is '创建人';

comment on column sys_role_permission.update_user is '更新人';

comment on column sys_role_permission.logic_delete is '是否删除（0-否,1-是）';

alter table sys_role_permission
    owner to buriedpoint;

INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (1, 'b3379836bd0e4f4a8b233c4b4b2391d1', 'b13379836bd0e4f4a8b233c4b4b2391d', '2021-04-20 17:27:54.016000', '2021-04-20 17:45:51.489000', 'admin', 'admin', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (15, '1', '1', '2021-04-21 13:49:58.665000', '2021-04-21 13:49:58.665000', '123', '123', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (16, '2', '2', '2021-04-21 13:52:31.455000', '2021-04-21 13:52:31.455000', '123', '123', false);create table sys_user
(
    id           bigserial               not null
        constraint sys_user_pk
            primary key,
    username     varchar(30)             not null
        constraint sys_user_un_username
            unique,
    password     varchar(100)            not null,
    nickname     varchar(40),
    phone        varchar(20)
        constraint sys_user_un_phone
            unique,
    email        varchar(50)
        constraint sys_user_un_email
            unique,
    create_time  timestamp default now() not null,
    update_time  timestamp default now() not null,
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    bid          varchar(32)             not null
);

comment on table sys_user is '用户信息';

comment on column sys_user.id is '主键';

comment on column sys_user.username is '用户名';

comment on column sys_user.password is '密码';

comment on column sys_user.nickname is '昵称';

comment on column sys_user.phone is '手机号';

comment on column sys_user.email is '邮箱';

comment on column sys_user.create_time is '创建时间';

comment on column sys_user.update_time is '更新时间';

comment on column sys_user.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user.create_user is '创建人';

comment on column sys_user.update_user is '更新人';

comment on column sys_user.bid is '业务id';

alter table sys_user
    owner to buriedpoint;

create index sys_user_email_idx
    on sys_user (email);

create index sys_user_phone_idx
    on sys_user (phone);

create index sys_user_username_idx
    on sys_user (username);

INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (4, 'aaa', '$2a$10$8yEHHH5G0GS4cW07wCu3SuDlhK9jPO90.Lp2pdeRb2UeZovZWRQEW', 'laohu', '18883330333', '539@geely.com', '2021-04-20 17:15:21.656361', '2021-04-20 17:15:21.594000', false, null, null, '259aad25f5f84212b95e67a80b3574c2');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (5, 'admin', '$2a$10$sHuitqfBFbIqYyvjAHYLy.uASY1kwSlQNmjEWrNzJaWcxvGLICFE6', '管理员', '13210101010', '123@qq.com', '2021-04-20 17:17:37.208353', '2021-04-20 17:17:37.070000', false, null, null, '3597087cb92040a2b084e1577f6fbaef');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (6, '123', '$2a$10$susmISxDIlZCJAJVS45hAOlSwXkLpYbdP8MhZYEIZ.pmmV5tMLnw2', '11', '13512367543', '781203456@qq.com', '2021-04-21 11:37:53.072818', '2021-04-21 11:37:52.983000', false, null, null, '1b6325eb2cbc4ccf9952ff21254a3698');create table sys_user_app
(
    id           bigserial   not null
        constraint sys_user_app_pkey
            primary key,
    user_bid     varchar(32) not null,
    app_bid      varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    constraint sys_user_app_un
        unique (user_bid, app_bid)
);

comment on table sys_user_app is '用户app表';

comment on column sys_user_app.id is '主键';

comment on column sys_user_app.user_bid is '用户业务id';

comment on column sys_user_app.app_bid is '应用业务id';

comment on column sys_user_app.create_time is '创建时间';

comment on column sys_user_app.update_time is '更新时间';

comment on column sys_user_app.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_app.create_user is '创建人';

comment on column sys_user_app.update_user is '更新人';

alter table sys_user_app
    owner to buriedpoint;

INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, false, '123', null);create table sys_user_role
(
    id           bigserial   not null
        constraint sys_user_role_pkey
            primary key,
    user_bid     varchar(32) not null,
    role_bid     varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(50),
    update_user  varchar(50),
    constraint sys_user_role_un
        unique (user_bid, role_bid)
);

comment on table sys_user_role is '用户角色表';

comment on column sys_user_role.id is '主键';

comment on column sys_user_role.user_bid is '用户业务id';

comment on column sys_user_role.role_bid is '角色业务id';

comment on column sys_user_role.create_time is '创建时间';

comment on column sys_user_role.update_time is '更新时间';

comment on column sys_user_role.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_role.create_user is '创建人';

comment on column sys_user_role.update_user is '更新人';

alter table sys_user_role
    owner to buriedpoint;

INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-21 11:37:53.072818', null, false, '123', null);create table oauth_client_details
(
    client_id               varchar(50) not null
        constraint oauth_client_details_pk
            primary key,
    resource_ids            varchar,
    client_secret           varchar     not null,
    scope                   varchar     not null,
    authorized_grant_types  varchar     not null,
    web_server_redirect_uri varchar,
    authorities             varchar,
    access_token_validity   smallint    not null,
    refresh_token_validity  smallint    not null,
    additional_information  varchar(4096),
    autoapprove             varchar(2)
);

comment on table oauth_client_details is '客户端信息';

comment on column oauth_client_details.client_id is '客户端id';

comment on column oauth_client_details.resource_ids is '可选，资源id集合，多个资源用英文逗号隔开';

comment on column oauth_client_details.client_secret is '秘钥';

comment on column oauth_client_details.scope is '权限范围，比如 read，write等可自定义';

comment on column oauth_client_details.authorized_grant_types is '授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开';

comment on column oauth_client_details.web_server_redirect_uri is '客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的';

comment on column oauth_client_details.authorities is '权限值：如ROLE_USER';

comment on column oauth_client_details.access_token_validity is 'access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时';

comment on column oauth_client_details.refresh_token_validity is 'refresh_token的有效时间值(单位:秒)不填写框架(类refreshTokenValiditySeconds)默认30天';

comment on column oauth_client_details.additional_information is '预留字段，格式必须是json';

comment on column oauth_client_details.autoapprove is '该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作';

alter table oauth_client_details
    owner to buriedpoint;

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9e314b8e61714529a8996c13c9b92118', 'buried-point-rest-service', '$2a$10$PNhtUlRZKDiDwX9emwHDDeLLqvFkUTk9sBSY7RK9VxumtY5U7tv5O', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('9aff8ab069184f5390bcbe055025c84b', 'buried-point-rest-service', '$2a$10$f0Xvm59QFUglbw4enmPYkO47YV5mLENNP/3sUrSSt24enJKil/eT.', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');
INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('406739f89cf74198a58c3c0359dace7a', 'buried-point-rest-service', '$2a$10$dbrDMlLwV10HkC2/BUSDTuLthuoqqdsJGVFlA57b7CXV6.jBaXBae', 'write,read', 'refresh_token,client_credentials,password', null, '', 86400, 86400, '{}', '');create table sys_app
(
    id           bigserial    not null,
    app_id       varchar(255),
    app_secret   varchar(255),
    bid          varchar(255),
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    name         varchar(100) not null,
    description  varchar(255),
    create_user  varchar(32)  not null,
    update_user  varchar(32),
    logic_delete boolean   default false
);

comment on table sys_app is '应用';

comment on column sys_app.id is '主键ID';

comment on column sys_app.app_id is '应用标识';

comment on column sys_app.app_secret is '应用秘钥(明文)';

comment on column sys_app.bid is '业务id';

comment on column sys_app.create_time is '创建时间';

comment on column sys_app.update_time is '更新时间';

comment on column sys_app.name is '应用名称';

comment on column sys_app.description is '描述';

comment on column sys_app.create_user is '创建人';

comment on column sys_app.update_user is '更新人';

comment on column sys_app.logic_delete is '是否删除（false-否,true-是）';

alter table sys_app
    owner to buriedpoint;

INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (1, '9e314b8e61714529a8996c13c9b92118', '0e9d129a4cea481b8c0db3ba32791842', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, '', null, 'aaa', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (2, '9aff8ab069184f5390bcbe055025c84b', 'bac396d5f2444f35bdd0753de218a46b', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, '', null, 'admin', null, false);
INSERT INTO public.sys_app (id, app_id, app_secret, bid, create_time, update_time, name, description, create_user, update_user, logic_delete) VALUES (3, '406739f89cf74198a58c3c0359dace7a', '73654353655149f29fd41bc7818576c9', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, '', null, '123', null, false);create table sys_permission
(
    id           bigserial                                  not null
        constraint sys_permission_pkey
            primary key,
    parent_bid   varchar(32)  default ''::character varying not null,
    name         varchar(64)                                not null,
    enname       varchar(64)                                not null,
    url          varchar(255)                               not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now()                 not null,
    update_time  timestamp    default now()                 not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)                                not null
);

comment on table sys_permission is '权限表';

comment on column sys_permission.id is '主键';

comment on column sys_permission.parent_bid is '父业务id';

comment on column sys_permission.name is '权限名称';

comment on column sys_permission.enname is '权限英文名称';

comment on column sys_permission.url is '授权路径';

comment on column sys_permission.description is '备注';

comment on column sys_permission.create_time is '创建时间';

comment on column sys_permission.update_time is '更新时间';

comment on column sys_permission.create_user is '创建人';

comment on column sys_permission.update_user is '更新人';

comment on column sys_permission.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_permission.bid is '业务id';

alter table sys_permission
    owner to buriedpoint;

INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzzneg', 'dd', '这是新增的权限', '2021-04-21 13:55:24.931000', '2021-04-21 13:55:24.931000', '123', '123', false, '99bb710df8704791a41b2ac06c909e4d');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegee', 'dd', '这是新增的权限', '2021-04-21 13:58:28.352000', '2021-04-21 13:58:28.352000', '123', '123', false, '09c9d7c2e8854f72932b2bcc9a093f04');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeee', 'dd', '这是新增的权限', '2021-04-21 13:59:06.876000', '2021-04-21 14:16:04.383000', '123', '123', false, '4e01101092134778a0352628e975ee58');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, 'b13379836bd0e4f4a8b233c4b4b2391d', 'fh', 'xinzznegeeeeee', '收到', '这是新增的权限', '2021-04-21 14:16:29.093000', '2021-04-21 14:17:20.331000', '123', '123', true, '745a690f84334f79af9e09ecac86cdd5');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '', 'asdf112', 'abc', '/ab', '', '2021-04-20 17:27:42.115569', '2021-04-21 15:48:28.997000', 'admin', 'admin', false, 'fbaaeca6b3694a1e81bfef28241a64cf');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '', '用户管理', 'abc_user', '/sfa', '', '2021-04-22 14:50:54.824000', '2021-04-22 15:07:11.643000', 'admin', 'admin', false, '4a33db1dac8c429fa796181ff9dfb8b4');
INSERT INTO public.sys_permission (id, parent_bid, name, enname, url, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '', '', '/t', '', '2021-04-22 17:31:26.310000', '2021-04-22 17:36:16.134000', '123', '123', false, '0464c5376b814a55a37cb44c8de46ed1');create table sys_role
(
    id           bigserial                  not null
        constraint sys_role_pkey
            primary key,
    parent_bid   varchar(32)  default 0,
    name         varchar(64)                not null,
    enname       varchar(64)                not null,
    description  varchar(200) default NULL::character varying,
    create_time  timestamp    default now() not null,
    update_time  timestamp    default now() not null,
    create_user  varchar(50),
    update_user  varchar(50),
    logic_delete boolean      default false,
    bid          varchar(32)
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键';

comment on column sys_role.parent_bid is '父业务id';

comment on column sys_role.name is '角色名称';

comment on column sys_role.enname is '角色英文名称';

comment on column sys_role.description is '备注';

comment on column sys_role.create_time is '创建时间';

comment on column sys_role.update_time is '更新时间';

comment on column sys_role.create_user is '创建人';

comment on column sys_role.update_user is '更新人';

comment on column sys_role.logic_delete is '是否删除（false-否,true-是）';

comment on column sys_role.bid is '业务id';

alter table sys_role
    owner to buriedpoint;

INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (3, '', '普通用户', 'liwen', '这是新增角色李文', '2021-04-21 12:55:11.020000', '2021-04-21 12:55:11.020000', '123', '123', false, '48f9c475700f45bc9fc536dfa1ef78c4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (4, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen1', '这是新增角色李文', '2021-04-21 12:56:30.457000', '2021-04-21 12:56:30.457000', '123', '123', false, 'b55679d09af849aaa233168c57cd5cda');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (5, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen2', '这是新增角色李文', '2021-04-21 13:04:58.874000', '2021-04-21 13:04:58.874000', '123', '123', false, '487c454b40cc475bb5be03b93d0001e4');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (7, '', '超级用户', 'zhangdg', '这是新增角色李文11111', '2021-04-21 13:33:37.736000', '2021-04-21 13:33:37.736000', '123', '123', false, '4e1c02e7d6fc4e2aacd468e759d49dce');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (8, '', '测试角色112', 'USER112', 'abc', '2021-04-21 13:37:56.600000', '2021-04-21 13:37:56.600000', 'admin', 'admin', false, '52f2f0bf64954bb5afebf681d105090e');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (1, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'USER', null, '2021-04-20 17:09:20.002839', '2021-04-20 17:09:20.002839', 'zz', null, false, 'b3379836bd0e4f4a8b233c4b4b2391dc');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (2, '48f9c475700f45bc9fc536dfa1ef78c4', '用户1', 'USER1', null, '2021-04-20 17:23:34.293602', '2021-04-20 17:23:34.293602', null, null, false, 'b3379836bd0e4f4a8b233c4b4b2391d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (10, '', '角色121', 'USER121', 'abc', '2021-04-21 13:51:33.516000', '2021-04-21 13:51:33.516000', 'admin', 'admin', true, '6f9e5aa78a1c4c57940a538f9a3c3d58');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (11, '1', '1', '1', '1', '2021-04-21 14:04:27.810000', '2021-04-21 14:04:27.810000', '123', '123', true, 'b001fd724daf4fe2bf5c32528c5c24d1');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (12, '', '1', '1', '', '2021-04-22 15:28:06.922000', '2021-04-22 15:28:06.922000', 'admin', 'admin', true, '12d32de42eb04b25b8e50b7185a92bc8');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (13, '', '1', '1', '', '2021-04-22 15:28:34.373000', '2021-04-22 15:28:34.373000', 'admin', 'admin', false, '2e1151600a2345f3a10b6a6b7cf86087');
INSERT INTO public.sys_role (id, parent_bid, name, enname, description, create_time, update_time, create_user, update_user, logic_delete, bid) VALUES (6, '48f9c475700f45bc9fc536dfa1ef78c4', '普通用户', 'liwen3', '这是新增角色李文888', '2021-04-21 13:12:48.444000', '2021-04-22 17:40:11.409000', '123', '123', false, '1bb9cf6be1de4fe1bb1cbed84d535955');create table sys_role_permission
(
    id             bigserial               not null
        constraint sys_role_permission_pkey
            primary key,
    role_bid       varchar(32)             not null,
    permission_bid varchar(32)             not null,
    create_time    timestamp default now() not null,
    update_time    timestamp default now(),
    create_user    varchar(50),
    update_user    varchar(50),
    logic_delete   boolean   default false
);

comment on table sys_role_permission is '角色权限表';

comment on column sys_role_permission.id is '主键';

comment on column sys_role_permission.role_bid is '角色业务id';

comment on column sys_role_permission.permission_bid is '权限业务id';

comment on column sys_role_permission.create_time is '创建时间';

comment on column sys_role_permission.update_time is '更新时间';

comment on column sys_role_permission.create_user is '创建人';

comment on column sys_role_permission.update_user is '更新人';

comment on column sys_role_permission.logic_delete is '是否删除（0-否,1-是）';

alter table sys_role_permission
    owner to buriedpoint;

INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (1, 'b3379836bd0e4f4a8b233c4b4b2391d1', 'b13379836bd0e4f4a8b233c4b4b2391d', '2021-04-20 17:27:54.016000', '2021-04-20 17:45:51.489000', 'admin', 'admin', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (15, '1', '1', '2021-04-21 13:49:58.665000', '2021-04-21 13:49:58.665000', '123', '123', false);
INSERT INTO public.sys_role_permission (id, role_bid, permission_bid, create_time, update_time, create_user, update_user, logic_delete) VALUES (16, '2', '2', '2021-04-21 13:52:31.455000', '2021-04-21 13:52:31.455000', '123', '123', false);create table sys_user
(
    id           bigserial               not null
        constraint sys_user_pk
            primary key,
    username     varchar(30)             not null
        constraint sys_user_un_username
            unique,
    password     varchar(100)            not null,
    nickname     varchar(40),
    phone        varchar(20)
        constraint sys_user_un_phone
            unique,
    email        varchar(50)
        constraint sys_user_un_email
            unique,
    create_time  timestamp default now() not null,
    update_time  timestamp default now() not null,
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    bid          varchar(32)             not null
);

comment on table sys_user is '用户信息';

comment on column sys_user.id is '主键';

comment on column sys_user.username is '用户名';

comment on column sys_user.password is '密码';

comment on column sys_user.nickname is '昵称';

comment on column sys_user.phone is '手机号';

comment on column sys_user.email is '邮箱';

comment on column sys_user.create_time is '创建时间';

comment on column sys_user.update_time is '更新时间';

comment on column sys_user.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user.create_user is '创建人';

comment on column sys_user.update_user is '更新人';

comment on column sys_user.bid is '业务id';

alter table sys_user
    owner to buriedpoint;

create index sys_user_email_idx
    on sys_user (email);

create index sys_user_phone_idx
    on sys_user (phone);

create index sys_user_username_idx
    on sys_user (username);

INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (4, 'aaa', '$2a$10$8yEHHH5G0GS4cW07wCu3SuDlhK9jPO90.Lp2pdeRb2UeZovZWRQEW', 'laohu', '18883330333', '539@geely.com', '2021-04-20 17:15:21.656361', '2021-04-20 17:15:21.594000', false, null, null, '259aad25f5f84212b95e67a80b3574c2');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (5, 'admin', '$2a$10$sHuitqfBFbIqYyvjAHYLy.uASY1kwSlQNmjEWrNzJaWcxvGLICFE6', '管理员', '13210101010', '123@qq.com', '2021-04-20 17:17:37.208353', '2021-04-20 17:17:37.070000', false, null, null, '3597087cb92040a2b084e1577f6fbaef');
INSERT INTO public.sys_user (id, username, password, nickname, phone, email, create_time, update_time, logic_delete, create_user, update_user, bid) VALUES (6, '123', '$2a$10$susmISxDIlZCJAJVS45hAOlSwXkLpYbdP8MhZYEIZ.pmmV5tMLnw2', '11', '13512367543', '781203456@qq.com', '2021-04-21 11:37:53.072818', '2021-04-21 11:37:52.983000', false, null, null, '1b6325eb2cbc4ccf9952ff21254a3698');create table sys_user_app
(
    id           bigserial   not null
        constraint sys_user_app_pkey
            primary key,
    user_bid     varchar(32) not null,
    app_bid      varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(32),
    update_user  varchar(32),
    constraint sys_user_app_un
        unique (user_bid, app_bid)
);

comment on table sys_user_app is '用户app表';

comment on column sys_user_app.id is '主键';

comment on column sys_user_app.user_bid is '用户业务id';

comment on column sys_user_app.app_bid is '应用业务id';

comment on column sys_user_app.create_time is '创建时间';

comment on column sys_user_app.update_time is '更新时间';

comment on column sys_user_app.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_app.create_user is '创建人';

comment on column sys_user_app.update_user is '更新人';

alter table sys_user_app
    owner to buriedpoint;

INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', '41d53adef17848e6b17aa3664e3cca1a', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'e435624d04c14fd68b990a32b1f393d4', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_app (id, user_bid, app_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', '63347de20ce14495a519cafd9600ec08', '2021-04-21 11:37:53.072818', null, false, '123', null);create table sys_user_role
(
    id           bigserial   not null
        constraint sys_user_role_pkey
            primary key,
    user_bid     varchar(32) not null,
    role_bid     varchar(32) not null,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    logic_delete boolean   default false,
    create_user  varchar(50),
    update_user  varchar(50),
    constraint sys_user_role_un
        unique (user_bid, role_bid)
);

comment on table sys_user_role is '用户角色表';

comment on column sys_user_role.id is '主键';

comment on column sys_user_role.user_bid is '用户业务id';

comment on column sys_user_role.role_bid is '角色业务id';

comment on column sys_user_role.create_time is '创建时间';

comment on column sys_user_role.update_time is '更新时间';

comment on column sys_user_role.logic_delete is '是否删除（0-否,1-是）';

comment on column sys_user_role.create_user is '创建人';

comment on column sys_user_role.update_user is '更新人';

alter table sys_user_role
    owner to buriedpoint;

INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (1, '259aad25f5f84212b95e67a80b3574c2', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:15:21.656361', null, false, 'aaa', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (2, '3597087cb92040a2b084e1577f6fbaef', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-20 17:17:37.208353', null, false, 'admin', null);
INSERT INTO public.sys_user_role (id, user_bid, role_bid, create_time, update_time, logic_delete, create_user, update_user) VALUES (3, '1b6325eb2cbc4ccf9952ff21254a3698', 'b3379836bd0e4f4a8b233c4b4b2391dc', '2021-04-21 11:37:53.072818', null, false, '123', null);