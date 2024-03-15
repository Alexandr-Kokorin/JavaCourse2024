package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.mappers.ChatMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    @Qualifier("jdbcChatRepository")
    private ChatRepository chatRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(1, "test");

        var chat = chatRepository.find(1);

        assertThat(chat.name()).isEqualTo("test");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(1, "test");

        chatRepository.remove(1);

        var chat = chatRepository.find(1);

        assertThat(chat).isEqualTo(null);
    }

    @Test
    @Transactional
    @Rollback
    void updateStateTest() {
        chatRepository.add(1, "test");

        chatRepository.updateState(1, "TRACK");

        var chat = chatRepository.find(1);

        assertThat(chat.state()).isEqualTo("TRACK");
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        chatRepository.add(1, "test");

        var chat = chatRepository.find(1);

        String sql = "SELECT * FROM chat WHERE id=?";
        var expected = jdbcClient.sql(sql)
            .param(1)
            .query(new ChatMapper()).single();

        assertThat(chat.name()).isEqualTo(expected.name());
    }
}
