package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.githubDTO.Branch;
import edu.java.scrapper.clients.githubDTO.Commit;
import edu.java.scrapper.clients.githubDTO.Pull;
import edu.java.scrapper.clients.githubDTO.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.time.OffsetDateTime;
import java.util.Arrays;
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
                          "pushed_at": "2024-01-31T22:21:31Z"
                        }""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
        extension.stubFor(get(urlEqualTo("/repos/user/test/commits"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("""
                        [
                            {
                              "commit": {
                                "committer": {
                                    "date": "2024-03-09T21:48:54Z"
                                },
                                "message": "add hw5"
                              }
                            },
                            {
                              "commit": {
                                "committer": {
                                    "date": "2024-03-08T21:48:54Z"
                                },
                                "message": "add hw4"
                              }
                            }
                        ]""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
        extension.stubFor(get(urlEqualTo("/repos/user/test/pulls"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("""
                        [
                            {
                              "number": 5,
                              "title": "Hw5",
                              "created_at": "2024-03-15T21:35:59Z"
                            }
                        ]""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
        extension.stubFor(get(urlEqualTo("/repos/user/test/branches"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("""
                        [
                            {
                              "name": "hw5-additional"
                            },
                            {
                              "name": "hw5"
                            }
                        ]""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    public void gitHubTest() {
        var repository = new Repository(OffsetDateTime.parse("2024-01-31T22:21:31Z"));
        var commits = new Commit[] {
            new Commit(
                new Commit.InternalCommit(
                    new Commit.InternalCommit.Committer(OffsetDateTime.parse("2024-03-09T21:48:54Z")),
                    "add hw5"
                )),
            new Commit(
                new Commit.InternalCommit(
                    new Commit.InternalCommit.Committer(OffsetDateTime.parse("2024-03-08T21:48:54Z")),
                    "add hw4"
                ))
        };
        var pulls = new Pull[]{
            new Pull(5, "Hw5", OffsetDateTime.parse("2024-03-15T21:35:59Z"))
        };
        var branches = new Branch[] {
            new Branch("hw5-additional"),
            new Branch("hw5")
        };

        var response = new GitHubClient("http://localhost:8888").getInfo("user", "test");

        assertThat(Arrays.toString(response.commits()).equals(Arrays.toString(commits)) &&
            Arrays.toString(response.pulls()).equals(Arrays.toString(pulls)) &&
            Arrays.toString(response.branches()).equals(Arrays.toString(branches)) &&
            response.repository().equals(repository)).isTrue();
    }
}
