package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcLinkServiceTest extends IntegrationTest {

    @Autowired
    @Qualifier("jdbcLinkService")
    private LinkService linkService;
    @Autowired
    @Qualifier("jdbcChatRepository")
    private ChatRepository chatRepository;
    @Autowired
    @Qualifier("jdbcLinkRepository")
    private LinkRepository linkRepository;
    @Autowired
    @Qualifier("jdbcAssignmentRepository")
    private AssignmentRepository assignmentRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(1, "test");
        linkService.add(1, URI.create("http://test"));

        var link = linkRepository.findByURL(URI.create("http://test"));

        var assignment = assignmentRepository.find(1, link.id());

        assertThat(Objects.nonNull(assignment)).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(1, "test");
        linkService.add(1, URI.create("http://test"));

        linkService.remove(1, URI.create("http://test"));

        var links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void listAllTest() {
        chatRepository.add(1, "test");
        linkService.add(1, URI.create("http://test1"));
        linkService.add(1, URI.create("http://test2"));

        var links = linkService.listAll(1);

        assertThat(links.size()).isEqualTo(2);
    }
}
