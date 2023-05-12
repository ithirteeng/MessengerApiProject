create table public.chat_entity
(
    id            uuid not null
        primary key,
    avatar_id     uuid,
    chat_admin    uuid,
    chat_name     varchar(255),
    creation_date date,
    is_dialog     boolean
);

create table public.chat_user_entity
(
    id      uuid not null
        primary key,
    user_id uuid,
    chat_id uuid
        constraint fk_chat1
            references public.chat_entity
);

create table public.message_entity
(
    id            uuid not null
        primary key,
    creation_date date,
    message_text  varchar(500),
    chat_id       uuid
        constraint fk_chat2
            references public.chat_entity
);


create table public.file_entity
(
    id         uuid not null
        primary key,
    file_name  varchar(255),
    storage_id uuid,
    message_id uuid
        constraint fk_message
            references public.message_entity
);


