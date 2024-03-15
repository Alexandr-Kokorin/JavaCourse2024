create table link
(
    id              bigint generated always as identity,
    url             text                     not null,
    last_update     timestamp with time zone not null,
    last_check      timestamp with time zone not null,

    primary key (id)
)
