package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.dto.Assignment;
import edu.java.scrapper.domain.mappers.AssignmentMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcAssignmentRepository implements AssignmentRepository {

    @Autowired
    private JdbcClient jdbcClient;

    @Transactional
    @Override
    public void add(long chatId, long linkId) {
        String sql = "INSERT INTO assignment VALUES(?, ?)";
        jdbcClient.sql(sql)
            .param(chatId)
            .param(linkId)
            .update();
    }

    @Transactional
    @Override
    public void remove(long chatId, long linkId) {
        String sql = "DELETE FROM assignment WHERE chat_id=? AND link_id=?";
        jdbcClient.sql(sql)
            .param(chatId)
            .param(linkId)
            .update();
    }

    @Transactional
    @Override
    public Assignment find(long chatId, long linkId) {
        String sql = "SELECT * FROM assignment WHERE chat_id=? AND link_id=?";
        List<Assignment> list = jdbcClient.sql(sql)
            .param(chatId)
            .param(linkId)
            .query(new AssignmentMapper()).list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Transactional
    @Override
    public List<Assignment> findAllByChatId(long chatId) {
        String sql = "SELECT * FROM assignment WHERE chat_id=?";
        return jdbcClient.sql(sql)
            .param(chatId)
            .query(new AssignmentMapper()).list();
    }

    @Transactional
    @Override
    public List<Assignment> findAllByLinkId(long linkId) {
        String sql = "SELECT * FROM assignment WHERE link_id=?";
        return jdbcClient.sql(sql)
            .param(linkId)
            .query(new AssignmentMapper()).list();
    }
}
