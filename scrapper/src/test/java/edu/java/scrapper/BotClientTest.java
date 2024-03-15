package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.LinkUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class BotClientTest {

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(8888)).build();

    @BeforeEach
    public void configStub() {
        extension.stubFor(post(urlEqualTo("/links"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    public void updateTest() throws Exception {
        var expected = ResponseEntity.ok().build().getStatusCode();

        var response = new BotClient("http://localhost:8888")
            .sendUpdates(new LinkUpdate(new URI("http://test"), "test", new long[]{1, 2}))
            .getStatusCode();

        assertThat(response).isEqualTo(expected);
    }
}
