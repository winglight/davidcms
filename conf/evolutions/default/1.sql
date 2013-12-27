# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company_model (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  created_at                datetime,
  constraint pk_company_model primary key (id))
;

create table content_model (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  url                       varchar(255),
  phone_number              varchar(255),
  small_pic                 varchar(255),
  big_pic                   varchar(255),
  content_type              integer,
  language                  integer,
  created_at                datetime,
  constraint ck_content_model_content_type check (content_type in (0,1,2,3)),
  constraint ck_content_model_language check (language in (0,1)),
  constraint pk_content_model primary key (id))
;

create table user_model (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  email                     varchar(255),
  device_id                 varchar(255),
  password                  varchar(255),
  status                    varchar(8),
  user_role                 varchar(9),
  created_at                datetime,
  constraint ck_user_model_status check (status in ('ACTIVE','INACTIVE')),
  constraint ck_user_model_user_role check (user_role in ('ADMIN','DEVELOPER','USER')),
  constraint pk_user_model primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table company_model;

drop table content_model;

drop table user_model;

SET FOREIGN_KEY_CHECKS=1;

