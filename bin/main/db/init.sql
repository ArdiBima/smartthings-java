create table public.vendors
(
    id         serial
        primary key,
    brand_name varchar(100) not null
);

alter table public.vendors
    owner to postgres;

create table public.devices
(
    id                 serial
        primary key,
    vendor_id          integer
        references public.vendors,
    brand_name         varchar(100),
    device_name        varchar(100),
    device_description text,
    device_config_json jsonb,
    device_value       integer,
    created_at         timestamp,
    updated_at         timestamp,
    deleted_at         timestamp
);

alter table public.devices
    owner to postgres;

create table public.users
(
    id         serial
        primary key,
    name       varchar(100),
    dob        date,
    address    text,
    country    varchar(50),
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp
);

alter table public.users
    owner to postgres;

create table public.user_devices
(
    user_id   integer not null
        references public.users,
    device_id integer not null
        references public.devices,
    primary key (user_id, device_id)
);

alter table public.user_devices
    owner to postgres;

