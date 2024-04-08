package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
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
    @Rollback
    void removeTest() {
        chatService.add(1, "test");

        linkService.add(1, URI.create("http://test"));
        linkService.add(1, URI.create("http://test2"));

        chatService.remove(1);

        var links = linkRepository.findAllWithLimit(10);

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void getStateTest() {
        chatService.add(1, "test");

        var state = chatService.getState(1);

        assertThat(state.state()).isEqualTo("NONE");
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
