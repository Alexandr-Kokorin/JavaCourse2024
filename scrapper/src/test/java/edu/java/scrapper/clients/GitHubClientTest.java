package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.githubDTO.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class GitHubClientTest {

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(8888)).build();

    @BeforeEach
    public void configStubGitHub() {
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
    public void gitHubTest() {
        var repository = new Repository("test", OffsetDateTime.parse("2023-09-13T21:17:36Z"),
            OffsetDateTime.parse("2024-02-18T10:28:37Z"), OffsetDateTime.parse("2024-01-31T22:21:31Z"));

        var response = new GitHubClient("http://localhost:8888").getRepositoryInfo("user", "test").block();

        assertThat(response).isEqualTo(repository);
    }
}
