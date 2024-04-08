package edu.java.scrapper.domain.jooq;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.mappers.LinkMapper;
import io.swagger.v3.core.util.Json;
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
public class JooqLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    void addTest() throws JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        var links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() throws JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        var links = linkRepository.findAllWithLimit(1);

        linkRepository.remove(links.get(0).id());

        links = linkRepository.findAllWithLimit(1);

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void updateData() throws InterruptedException, JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        Thread.sleep(10);

        var links1 = linkRepository.findAllWithLimit(1);

        test = new StackOverflowData(3);

        linkRepository.updateData(links1.get(0).id(), Json.mapper().writeValueAsString(test));

        var links2 = linkRepository.findAllWithLimit(1);

        assertThat(Json.mapper().writeValueAsString(test)).isEqualTo(links2.get(0).data());
    }

    @Test
    @Transactional
    @Rollback
    void updateTimeOfLastUpdateTest() throws InterruptedException, JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        Thread.sleep(10);

        var links1 = linkRepository.findAllWithLimit(1);

        linkRepository.updateTimeOfLastUpdate(links1.get(0).id(), OffsetDateTime.now());

        var links2 = linkRepository.findAllWithLimit(1);

        assertThat(links1.get(0).lastUpdate().isBefore(links2.get(0).lastUpdate())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void updateTimeOfLastCheckTest() throws InterruptedException, JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        Thread.sleep(10);

        var links1 = linkRepository.findAllWithLimit(1);

        linkRepository.updateTimeOfLastCheck(links1.get(0).id(), OffsetDateTime.now());

        var links2 = linkRepository.findAllWithLimit(1);

        assertThat(links1.get(0).lastCheck().isBefore(links2.get(0).lastCheck())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() throws JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

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
    void findByURLTest() throws JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

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
    void findAllWithLimitTest() throws JsonProcessingException {
        var test = new StackOverflowData(2);
        linkRepository.add(URI.create("http://test1"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));
        linkRepository.add(URI.create("http://test2"), OffsetDateTime.now(), "test", Json.mapper().writeValueAsString(test));

        var links = linkRepository.findAllWithLimit(10);

        assertThat(links.size()).isEqualTo(2);
    }
}
