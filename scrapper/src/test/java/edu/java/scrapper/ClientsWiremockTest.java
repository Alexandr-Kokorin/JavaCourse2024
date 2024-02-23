package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.githubDTO.Repository;
import edu.java.scrapper.clients.stackoverflowDTO.Item;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.annotation.DirtiesContext;
import java.time.OffsetDateTime;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class ClientsWiremockTest {

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(80)).build();

    @Test
    @DirtiesContext
    public void gitHubTest() {
        configStubGitHub();
        var repository = new Repository("test", OffsetDateTime.parse("2023-09-13T21:17:36Z"),
            OffsetDateTime.parse("2024-02-18T10:28:37Z"), OffsetDateTime.parse("2024-01-31T22:21:31Z"));

        var response = new GitHubClient("localhost").getRepositoryInfo("user", "test").block();

        assertThat(response).isEqualTo(repository);
    }

    private void configStubGitHub() {
        configureFor("localhost", 80);
        extension.stubFor(get(urlEqualTo("/repos/user/test"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("""
                        {
                          "name": "test",
                          "created_at": "2023-09-13T21:17:36Z",
                          "updated_at": "2024-02-18T10:28:37Z",
                          "pushed_at": "2024-01-31T22:21:31Z"
                        }""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    @DirtiesContext
    public void stackOverflowTest() {
        configStubStackOverflow();
        var question = new Question(
            List.of(
                new Item(OffsetDateTime.parse("2014-12-24T18:44:37Z"), OffsetDateTime.parse("2014-12-24T18:44:37Z"),
                    OffsetDateTime.parse("2011-12-13T17:20:09Z"), 8493347)
            )
        );

        var response = new StackOverflowClient("localhost").getQuestionInfo(1).block();

        assertThat(response).isEqualTo(question);
    }

    private void configStubStackOverflow() {
        configureFor("localhost", 80);
        extension.stubFor(get(urlEqualTo("/2.3/questions/1/answers?site=stackoverflow&filter=withbody"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\"items\":[{\"last_activity_date\":1419446677,\"last_edit_date\":1419446677,\"creation_date\":1323796809,\"answer_id\":8493347}]}")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }
}
