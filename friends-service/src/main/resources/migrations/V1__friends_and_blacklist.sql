create table public.blacklist_table
(
    id               uuid not null
        primary key,
    add_note_date    date,
    adding_user      uuid,
    delete_note_date date,
    full_name        varchar(255),
    target_user      uuid
);

create table public.friend_table
(
    id                 uuid not null
        primary key,
    add_friend_date    date,
    adding_user        uuid,
    delete_friend_date date,
    full_name          varchar(255),
    target_user        uuid
);

