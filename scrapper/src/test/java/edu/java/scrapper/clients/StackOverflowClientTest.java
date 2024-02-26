package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.stackoverflowDTO.Item;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.time.OffsetDateTime;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class StackOverflowClientTest {

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(8888)).build();

    @BeforeEach
    public void configStubStackOverflow() {
        extension.stubFor(get(urlEqualTo("/2.3/questions/1/answers?site=stackoverflow&filter=withbody"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\"items\":[{\"last_activity_date\":1419446677,\"last_edit_date\":1419446677,\"creation_date\":1323796809,\"answer_id\":8493347}]}")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    public void stackOverflowTest() {
        var question = new Question(
            List.of(
                new Item(OffsetDateTime.parse("2014-12-24T18:44:37Z"), OffsetDateTime.parse("2014-12-24T18:44:37Z"),
                    OffsetDateTime.parse("2011-12-13T17:20:09Z"), 8493347)
            )
        );

        var response = new StackOverflowClient("http://localhost:8888").getQuestionInfo(1).block();

        assertThat(response).isEqualTo(question);
    }
}
