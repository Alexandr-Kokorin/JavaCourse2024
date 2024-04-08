package edu.java.bot;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.configuration.MessageDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@WireMockTest
public class BotMessageTest {

    @Autowired
    private DialogManager dialogManager;

    @RegisterExtension
    public static WireMockExtension extension = WireMockExtension.newInstance().options(wireMockConfig().port(8080)).build();

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
    void startCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.startMessagePart1, MessageDatabase.startMessagePart2);

        List<String> result = dialogManager.sortMessage(1, "/start");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void trackCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.trackMessagePart1);

        List<String> result = dialogManager.sortMessage(1, "/track");

        assertThat(result).isEqualTo(list);
    }

    private void getStateTrackConfigStub() {
        extension.stubFor(get(urlEqualTo("/state/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\"state\": \"TRACK\"}")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    void trackCommandTestPart2() {
        getStateTrackConfigStub();

        List<String> list = List.of(MessageDatabase.trackMessageError1);

        List<String> result = dialogManager.sortMessage(1, "https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void untrackCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.untrackMessagePart1);

        List<String> result = dialogManager.sortMessage(1, "/untrack");

        assertThat(result).isEqualTo(list);
    }

    private void getStateUntrackConfigStub() {
        extension.stubFor(get(urlEqualTo("/state/1"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody("{\"state\": \"UNTRACK\"}")
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        );
    }

    @Test
    void untrackCommandTestPart2() {
        getStateUntrackConfigStub();

        List<String> list = List.of(MessageDatabase.untrackMessagePart2);

        List<String> result = dialogManager.sortMessage(1, "https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void listCommandTest() {
        List<String> list = List.of(MessageDatabase.listMessage, "1) http://test\n");

        List<String> result = dialogManager.sortMessage(1, "/list");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void helpCommandTest() {
        List<String> list = List.of(MessageDatabase.helpMessage);

        List<String> result = dialogManager.sortMessage(1, "/help");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void unidentifiedCommandTest() {
        List<String> list = List.of(MessageDatabase.unidentifiedMessage);

        List<String> result = dialogManager.sortMessage(1, "/stop");

        assertThat(result).isEqualTo(list);
    }
}
