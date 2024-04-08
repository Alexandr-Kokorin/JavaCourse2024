package edu.java.scrapper.service.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jooq.JooqAssignmentRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JooqLinkServiceTest extends IntegrationTest {

    @Autowired
    private LinkService linkService;
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
