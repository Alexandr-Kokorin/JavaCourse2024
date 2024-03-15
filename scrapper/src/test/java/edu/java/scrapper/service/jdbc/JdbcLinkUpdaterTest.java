package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcLinkUpdaterTest extends IntegrationTest {

    @Autowired
    @Qualifier("jdbcLinkUpdater")
    private LinkUpdater linkUpdater;
    @Autowired
    @Qualifier("jdbcLinkService")
    private LinkService linkService;
    @Autowired
    @Qualifier("jdbcChatRepository")
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void getLinksTest() {
        chatRepository.add(1, "test");
        linkService.add(1, URI.create("http://test1"));
        linkService.add(1, URI.create("http://test2"));

        var links = linkUpdater.getLinks(2);

        assertThat(links.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() throws InterruptedException {
        chatRepository.add(1, "test");
        linkService.add(1, URI.create("http://test1"));

        var links = linkUpdater.getLinks(1);

        Thread.sleep(100);

        var response = linkUpdater.update(links.get(0));

        assertThat(response.url()).isEqualTo(URI.create("http://test1"));
    }
}
