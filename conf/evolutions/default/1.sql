# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table image_entry (
  id                            bigint not null,
  image                         blob,
  constraint pk_image_entry primary key (id)
);
create sequence image_entry_seq;


# --- !Downs

drop table if exists image_entry;
drop sequence if exists image_entry_seq;

