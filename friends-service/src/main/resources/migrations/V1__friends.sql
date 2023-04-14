create table public.blacklist_table
(
    id               uuid not null
        primary key,
    add_note_date    timestamp,
    adding_user      uuid,
    delete_note_date timestamp,
    full_name        varchar(255),
    target_user      uuid
);

alter table public.blacklist_table
    owner to ithirteeng;

create table public.friend_table
(
    id                 uuid not null
        primary key,
    add_friend_date    timestamp,
    adding_user        uuid,
    delete_friend_date timestamp,
    full_name          varchar(255),
    target_user        uuid
);

alter table public.friend_table
    owner to ithirteeng;

