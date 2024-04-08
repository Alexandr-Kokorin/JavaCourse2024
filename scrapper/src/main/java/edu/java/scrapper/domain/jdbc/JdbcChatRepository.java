package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.mappers.ChatMapper;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements ChatRepository {

    @Autowired
    private JdbcClient jdbcClient;

    @Transactional
    @Override
    public void add(long id, String name) {
        String sql = "INSERT INTO chat VALUES(?, ?, ?, ?)";
        jdbcClient.sql(sql)
            .param(id)
            .param(name)
            .param("NONE")
            .param(OffsetDateTime.now())
            .update();
    }

    @Transactional
    @Override
    public void remove(long id) {
        String sql = "DELETE FROM chat WHERE id=?";
        jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Transactional
    @Override
    public void updateState(long id, String state) {
        String sql = "UPDATE chat SET state=? WHERE id=?";
        jdbcClient.sql(sql)
            .param(state)
            .param(id)
            .update();
    }

    @Transactional
    @Override
    public Chat find(long id) {
        String sql = "SELECT * FROM chat WHERE id=?";
        List<Chat> list = jdbcClient.sql(sql)
            .param(id)
            .query(new ChatMapper()).list();
        return list.isEmpty() ? null : list.get(0);
    }
}
