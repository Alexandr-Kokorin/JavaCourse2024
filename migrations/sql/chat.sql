create table chat
(
    id              bigint                   not null,
    name            text                     not null,
    state           text                     not null,
    created_at      timestamp with time zone not null,

    primary key (id)
)
