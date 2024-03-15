create table assignment
(
    chat_id      bigint      not null references chat(id) on delete cascade,
    link_id      bigint      not null references link(id) on delete cascade
)
