create table public.notification_entity
(
    id           uuid not null
        primary key,
    read_time    timestamp,
    receive_time timestamp,
    status       integer,
    text         varchar(255),
    type         integer,
    user_id      uuid
);




