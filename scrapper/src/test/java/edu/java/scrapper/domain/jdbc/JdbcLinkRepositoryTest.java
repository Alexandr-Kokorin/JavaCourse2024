package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.mappers.LinkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {

    @Autowired
    @Qualifier("jdbcLinkRepository")
    private LinkRepository linkRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        var links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        var links = linkRepository.findAllWithLimit(1);

        linkRepository.remove(links.get(0).id());

        links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void updateTimeOfLastUpdateTest() throws InterruptedException {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        Thread.sleep(10);

        var links1 = linkRepository.findAllWithLimit(1);

        linkRepository.updateTimeOfLastUpdate(links1.get(0).id(), OffsetDateTime.now());

        var links2 = linkRepository.findAllWithLimit(1);

        assertThat(links1.get(0).lastUpdate().isBefore(links2.get(0).lastUpdate())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void updateTimeOfLastCheckTest() throws InterruptedException {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        Thread.sleep(10);

        var links1 = linkRepository.findAllWithLimit(1);

        linkRepository.updateTimeOfLastCheck(links1.get(0).id(), OffsetDateTime.now());

        var links2 = linkRepository.findAllWithLimit(1);

        assertThat(links1.get(0).lastCheck().isBefore(links2.get(0).lastCheck())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        var links = linkRepository.findAllWithLimit(1);

        var link1 = linkRepository.findById(links.get(0).id());

        String sql = "SELECT * FROM link WHERE id=?";
        var link2 = jdbcClient.sql(sql)
            .param(links.get(0).id())
            .query(new LinkMapper()).single();

        assertThat(link1.url()).isEqualTo(link2.url());
    }

    @Test
    @Transactional
    @Rollback
    void findByURLTest() {
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now());

        var link1 = linkRepository.findByURL(URI.create("http://test"));

        String sql = "SELECT * FROM link WHERE url=?";
        var link2 = jdbcClient.sql(sql)
            .param("http://test")
            .query(new LinkMapper()).single();

        assertThat(link1.id()).isEqualTo(link2.id());
    }

    @Test
    @Transactional
    @Rollback
    void findAllWithLimitTest() {
        linkRepository.add(URI.create("http://test1"), OffsetDateTime.now());
        linkRepository.add(URI.create("http://test2"), OffsetDateTime.now());

        var links = linkRepository.findAllWithLimit(10);

        assertThat(links.size()).isEqualTo(2);
    }
}
