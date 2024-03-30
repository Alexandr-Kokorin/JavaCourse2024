package edu.java.scrapper.domain.jooq;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.dto.Assignment;
import edu.java.scrapper.domain.mappers.AssignmentMapper;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JooqAssignmentRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqAssignmentRepository assignmentRepository;
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    @Transactional
    @Rollback
    void createLinksAndChats() throws JsonProcessingException {
        chatRepository.add(1, "test1");
        chatRepository.add(2, "test2");
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test1"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));
        linkRepository.add(URI.create("http://test2"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        var list = linkRepository.findAllWithLimit(2);

        assignmentRepository.add(1, list.get(0).id());

        var assignment = assignmentRepository.find(1, list.get(0).id());

        assertThat(assignment).isEqualTo(new Assignment(1, list.get(0).id()));
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        var list = linkRepository.findAllWithLimit(2);

        assignmentRepository.add(1, list.get(0).id());

        assignmentRepository.remove(1, list.get(0).id());

        var assignment = assignmentRepository.find(1, list.get(0).id());

        assertThat(assignment).isEqualTo(null);
    }

    @Test
    @Transactional
    @Rollback
    void findTest() {
        var list = linkRepository.findAllWithLimit(2);

        assignmentRepository.add(1, list.get(0).id());

        var assignment = assignmentRepository.find(1, list.get(0).id());

        String sql = "SELECT * FROM assignment WHERE chat_id=? AND link_id=?";
        var expected = jdbcClient.sql(sql)
            .param(1)
            .param(list.get(0).id())
            .query(new AssignmentMapper()).single();

        assertThat(assignment).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByChatIdTest() {
        var list = linkRepository.findAllWithLimit(2);

        assignmentRepository.add(1, list.get(0).id());
        assignmentRepository.add(1, list.get(1).id());

        var assignments = assignmentRepository.findAllByChatId(1);

        assertThat(assignments.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByLinkIdTest() {
        var list = linkRepository.findAllWithLimit(2);

        assignmentRepository.add(1, list.get(0).id());
        assignmentRepository.add(2, list.get(0).id());

        var assignments = assignmentRepository.findAllByLinkId(list.get(0).id());

        assertThat(assignments.size()).isEqualTo(2);
    }
}
