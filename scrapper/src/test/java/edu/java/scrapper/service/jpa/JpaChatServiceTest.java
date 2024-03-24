package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {

    @Autowired
    @Qualifier("jpaChatService")
    private ChatService chatService;
    @Autowired
    @Qualifier("jpaLinkService")
    private LinkService linkService;
    @Autowired
    @Qualifier("jdbcChatRepository")
    private ChatRepository chatRepository;

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

        linkService.add(1, URI.create("http://test"));

        chatService.remove(1);

        var chat = chatRepository.find(1);

        assertThat(chat).isEqualTo(null);
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
