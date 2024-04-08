package edu.java.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.states.DialogState;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.StateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
@SpringBootTest
public class ScrapperClientTest {

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(8888)).build();
    @Autowired
    private ScrapperClient scrapperClient;

    @BeforeEach
    public void configStub() {
        addTgChatConfigStub();
        deleteTgChatConfigStub();
        getStateConfigStub();
        updateStateConfigStub();
        getLinksConfigStub();
        addLinkConfigStub();
        deleteLinkConfigStub();
    }

    private void addTgChatConfigStub() {
        extension.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void deleteTgChatConfigStub() {
        extension.stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void getStateConfigStub() {
        extension.stubFor(get(urlEqualTo("/state/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\"state\": \"NONE\"}")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void updateStateConfigStub() {
        extension.stubFor(post(urlEqualTo("/state/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void getLinksConfigStub() {
        extension.stubFor(get(urlEqualTo("/links"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("""
                        {
                          "links": [
                            {
                              "id": 1,
                              "url": "http://test"
                            }
                          ],
                          "size": 1
                        }""")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void addLinkConfigStub() {
        extension.stubFor(post(urlEqualTo("/links/add"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    private void deleteLinkConfigStub() {
        extension.stubFor(post(urlEqualTo("/links/delete"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    public void addTgChatTest() {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new ScrapperClient("http://localhost:8888").addTgChat(1, "name").getStatusCode();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void deleteTgChatTest() {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new ScrapperClient("http://localhost:8888").deleteTgChat(1).getStatusCode();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void getStateTest() {
        var expected = ResponseEntity.ok(new StateResponse("NONE")).getBody();

        var response = new ScrapperClient("http://localhost:8888").getState(1).getBody();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void updateStateTest() {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new ScrapperClient("http://localhost:8888").updateState(1, DialogState.TRACK).getStatusCode();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void getLinksTest() throws URISyntaxException {
        var expected = ResponseEntity.ok(new ListLinksResponse(new LinkResponse[]{new LinkResponse(1, new URI("http://test"))}, 1)).getBody();

        var response = new ScrapperClient("http://localhost:8888").getLinks(1).getBody();

        assertThat(response.links()[0]).isEqualTo(expected.links()[0]);
    }

    @Test
    public void addLinkTest() throws URISyntaxException, JsonProcessingException {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new ScrapperClient("http://localhost:8888").addLink(1, new URI("http://test")).getStatusCode();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void deleteLinkTest() throws URISyntaxException, JsonProcessingException {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new ScrapperClient("http://localhost:8888").deleteLink(1, new URI("http://test")).getStatusCode();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void retryTest() {
        var expected = new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE).getStatusCode();

        var response = scrapperClient.getState(1).getStatusCode();

        assertThat(response).isEqualTo(expected);
    }
}
