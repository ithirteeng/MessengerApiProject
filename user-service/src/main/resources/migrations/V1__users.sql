create table public.user_table
(
    id                uuid not null
        primary key,
    avatar_id         uuid,
    birth_date        date,
    city              varchar(255),
    email             varchar(255)
        constraint uk_email_constraint
            unique,
    full_name         varchar(255),
    login             varchar(255)
        constraint uk_login_constraint
            unique,
    password          varchar(255),
    registration_date timestamp,
    telephone_number  varchar(255)
);

