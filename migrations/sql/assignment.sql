create table assignment
(
    chat_id      bigint      not null references chat(id),
    link_id      bigint      not null references link(id)
)
