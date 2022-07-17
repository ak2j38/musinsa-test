DROP TABLE if exists category;

create table category
(
    id                 bigint auto_increment not null,
    created_at         timestamp,
    updated_at         timestamp,
    code               varchar(255),
    depth              integer,
    name               varchar(255),
    parent_category_id bigint,
    primary key (id)
)


