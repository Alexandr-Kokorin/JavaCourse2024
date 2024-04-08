package edu.java.scrapper.service.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jooq.JooqAssignmentRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JooqChatServiceTest extends IntegrationTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqAssignmentRepository assignmentRepository;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatService.add(1, "test");

        var chat = chatRepository.find(1);

        assertThat(chat.name()).isEqualTo("test");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatService.add(1, "test");
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "", "");

        var links = linkRepository.findAllWithLimit(1);

        assignmentRepository.add(1, links.get(0).id());

        chatService.remove(1);

        links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void getStateTest() {
        chatService.add(1, "test");

        chatRepository.updateState(1, "TEST");

        var state = chatService.getState(1);

        assertThat(state.state()).isEqualTo("TEST");
    }

    @Test
    @Transactional
    @Rollback
    void setStateTest() {
        chatService.add(1, "test");

        chatService.setState(1, "TEST");

        var state = chatService.getState(1);

        assertThat(state.state()).isEqualTo("TEST");
    }
}
