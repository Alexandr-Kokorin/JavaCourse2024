package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.jooq.generated.tables.Chat;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqChatRepository implements ChatRepository {

    @Autowired
    DSLContext context;

    @Transactional
    @Override
    public void add(long id, String name) {
        context.insertInto(Chat.CHAT)
            .values(id, name, "NONE", OffsetDateTime.now())
            .execute();
    }

    @Transactional
    @Override
    public void remove(long id) {
        context.deleteFrom(Chat.CHAT)
            .where(Chat.CHAT.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public void updateState(long id, String state) {
        context.update(Chat.CHAT)
            .set(Chat.CHAT.STATE, state)
            .where(Chat.CHAT.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public edu.java.scrapper.domain.dto.Chat find(long id) {
        var chat = context.selectFrom(Chat.CHAT)
            .where(Chat.CHAT.ID.eq(id))
            .fetchOne();
        return Objects.isNull(chat) ? null : chat.into(edu.java.scrapper.domain.dto.Chat.class);
    }
}
