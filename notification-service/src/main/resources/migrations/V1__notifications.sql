create table public.notification_entity
(
    id           uuid not null
        primary key,
    is_read      boolean,
    read_time    timestamp,
    receive_time timestamp,
    text         varchar(255),
    type         integer,
    user_id      uuid
);


