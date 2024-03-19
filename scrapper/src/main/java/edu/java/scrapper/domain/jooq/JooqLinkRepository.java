package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jooq.generated.tables.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqLinkRepository implements LinkRepository {

    @Autowired
    DSLContext context;

    @Transactional
    @Override
    public void add(URI url, OffsetDateTime lastUpdate, String type, String data) {
        context.insertInto(Link.LINK)
            .set(Link.LINK.URL, url.toString())
            .set(Link.LINK.TYPE, type)
            .set(Link.LINK.DATA, data)
            .set(Link.LINK.LAST_UPDATE, lastUpdate)
            .set(Link.LINK.LAST_CHECK, OffsetDateTime.now())
            .execute();
    }

    @Transactional
    @Override
    public void remove(long id) {
        context.deleteFrom(Link.LINK)
            .where(Link.LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public void updateData(long id, String data) {
        context.update(Link.LINK)
            .set(Link.LINK.DATA, data)
            .where(Link.LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public void updateTimeOfLastUpdate(long id, OffsetDateTime lastUpdate) {
        context.update(Link.LINK)
            .set(Link.LINK.LAST_UPDATE, lastUpdate)
            .where(Link.LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public void updateTimeOfLastCheck(long id, OffsetDateTime lastCheck) {
        context.update(Link.LINK)
            .set(Link.LINK.LAST_CHECK, lastCheck)
            .where(Link.LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public edu.java.scrapper.domain.dto.Link findById(long id) {
        var link = context.selectFrom(Link.LINK)
            .where(Link.LINK.ID.eq(id))
            .fetchOne();
        return Objects.isNull(link) ? null : link.into(edu.java.scrapper.domain.dto.Link.class);
    }

    @Transactional
    @Override
    public edu.java.scrapper.domain.dto.Link findByURL(URI url) {
        var link = context.selectFrom(Link.LINK)
            .where(Link.LINK.URL.eq(url.toString()))
            .fetchOne();
        return Objects.isNull(link) ? null : link.into(edu.java.scrapper.domain.dto.Link.class);
    }

    @Transactional
    @Override
    public List<edu.java.scrapper.domain.dto.Link> findAllWithLimit(int count) {
        return context.selectFrom(Link.LINK)
            .orderBy(Link.LINK.LAST_CHECK)
            .limit(count)
            .fetchInto(edu.java.scrapper.domain.dto.Link.class);
    }
}
