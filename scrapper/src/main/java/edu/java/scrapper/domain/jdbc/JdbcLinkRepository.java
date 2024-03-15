package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.mappers.LinkMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements LinkRepository {

    @Autowired
    private JdbcClient jdbcClient;

    @Transactional
    @Override
    public void add(URI url, OffsetDateTime lastUpdate) {
        String sql = "INSERT INTO link (url, last_update, last_check) VALUES (?, ?, ?)";
        jdbcClient.sql(sql)
            .param(url.toString())
            .param(lastUpdate)
            .param(OffsetDateTime.now())
            .update();
    }

    @Transactional
    @Override
    public void remove(long id) {
        String sql = "DELETE FROM link WHERE id=?";
        jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Transactional
    @Override
    public void updateTimeOfLastUpdate(long id, OffsetDateTime lastUpdate) {
        String sql = "UPDATE link SET last_update=? WHERE id=?";
        jdbcClient.sql(sql)
            .param(lastUpdate)
            .param(id)
            .update();
    }

    @Transactional
    @Override
    public void updateTimeOfLastCheck(long id, OffsetDateTime lastCheck) {
        String sql = "UPDATE link SET last_check=? WHERE id=?";
        jdbcClient.sql(sql)
            .param(lastCheck)
            .param(id)
            .update();
    }

    @Transactional
    @Override
    public Link findById(long id) {
        String sql = "SELECT * FROM link WHERE id=?";
        List<Link> list = jdbcClient.sql(sql)
            .param(id)
            .query(new LinkMapper()).list();
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public Link findByURL(URI url) {
        String sql = "SELECT * FROM link WHERE url=?";
        List<Link> list = jdbcClient.sql(sql)
            .param(url.toString())
            .query(new LinkMapper()).list();
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public List<Link> findAllWithLimit(int count) {
        String sql = "SELECT * FROM link ORDER BY last_check LIMIT ?";
        return jdbcClient.sql(sql)
            .param(count)
            .query(new LinkMapper()).list();
    }
}
