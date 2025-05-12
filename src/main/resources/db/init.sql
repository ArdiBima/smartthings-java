create table vendors
(
    id         serial
        primary key,
    brand_name varchar(100) not null
);

alter table vendors
    owner to postgres;

create table devices
(
    id                 serial
        primary key,
    vendor_id          integer
        references vendors,
    brand_name         varchar(100),
    device_name        varchar(100),
    device_description text,
    device_config_json jsonb,
    device_value       integer,
    created_at         timestamp,
    updated_at         timestamp,
    deleted_at         timestamp
);

alter table devices
    owner to postgres;

create table users
(
    id         serial
        primary key,
    name       varchar(100),
    dob        date,
    address    text,
    country    varchar(50),
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp,
    email      varchar(255) not null
        unique,
    password   varchar(255) not null
);

alter table users
    owner to postgres;

create table user_devices
(
    user_id   integer not null
        references users,
    device_id integer not null
        references devices,
    primary key (user_id, device_id)
);

alter table user_devices
    owner to postgres;

