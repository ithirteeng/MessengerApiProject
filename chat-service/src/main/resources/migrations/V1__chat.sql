create table public.chat_entity
(
    id            uuid not null primary key,
    avatar_id     uuid,
    chat_admin    uuid,
    chat_name     varchar(255),
    creation_date date,
    is_dialog     boolean
);
