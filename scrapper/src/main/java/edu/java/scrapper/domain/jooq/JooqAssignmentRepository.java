package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.jooq.generated.tables.Assignment;
import java.util.List;
import java.util.Objects;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqAssignmentRepository implements AssignmentRepository {

    @Autowired
    DSLContext context;

    @Transactional
    @Override
    public void add(long chatId, long linkId) {
        context.insertInto(Assignment.ASSIGNMENT)
            .values(chatId, linkId)
            .execute();
    }

    @Transactional
    @Override
    public void remove(long chatId, long linkId) {
        context.deleteFrom(Assignment.ASSIGNMENT)
            .where(Assignment.ASSIGNMENT.CHAT_ID.eq(chatId).and(Assignment.ASSIGNMENT.LINK_ID.eq(linkId)))
            .execute();
    }

    @Transactional
    @Override
    public edu.java.scrapper.domain.dto.Assignment find(long chatId, long linkId) {
        var assignment = context.selectFrom(Assignment.ASSIGNMENT)
            .where(Assignment.ASSIGNMENT.CHAT_ID.eq(chatId).and(Assignment.ASSIGNMENT.LINK_ID.eq(linkId)))
            .fetchOne();
        return Objects.isNull(assignment) ? null : assignment.into(edu.java.scrapper.domain.dto.Assignment.class);
    }

    @Transactional
    @Override
    public List<edu.java.scrapper.domain.dto.Assignment> findAllByChatId(long chatId) {
        return context.selectFrom(Assignment.ASSIGNMENT)
            .where(Assignment.ASSIGNMENT.CHAT_ID.eq(chatId))
            .fetchInto(edu.java.scrapper.domain.dto.Assignment.class);
    }

    @Transactional
    @Override
    public List<edu.java.scrapper.domain.dto.Assignment> findAllByLinkId(long linkId) {
        return context.selectFrom(Assignment.ASSIGNMENT)
            .where(Assignment.ASSIGNMENT.LINK_ID.eq(linkId))
            .fetchInto(edu.java.scrapper.domain.dto.Assignment.class);
    }
}
