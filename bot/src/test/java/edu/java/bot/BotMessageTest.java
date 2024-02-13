package edu.java.bot;

import edu.java.bot.configuration.MessageDatabase;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BotMessageTest {

    private final DialogManager dialogManager = new DialogManager();

    @Test
    void startCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.startMessagePart1, MessageDatabase.startMessagePart2);

        List<String> result = dialogManager.sortMessage(0, "/start");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void startCommandTestPart2() {
        List<String> list = List.of(MessageDatabase.gagMessage);

        List<String> result = dialogManager.sortMessage(0, "Alexandr");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void trackCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.trackMessagePart1);

        List<String> result = dialogManager.sortMessage(0, "/track");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void trackCommandTestPart2() {
        List<String> list = List.of(MessageDatabase.gagMessage);

        List<String> result = dialogManager.sortMessage(0, "https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void untrackCommandTestPart1() {
        List<String> list = List.of(MessageDatabase.untrackMessagePart1);

        List<String> result = dialogManager.sortMessage(0, "/untrack");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void untrackCommandTestPart2() {
        List<String> list = List.of(MessageDatabase.gagMessage);

        List<String> result = dialogManager.sortMessage(0, "https://github.com/sanyarnd/tinkoff-java-course-2023/");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void listCommandTest() {
        List<String> list = List.of(MessageDatabase.emptyListMessage);

        List<String> result = dialogManager.sortMessage(0, "/list");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void helpCommandTest() {
        List<String> list = List.of(MessageDatabase.helpMessage);

        List<String> result = dialogManager.sortMessage(0, "/help");

        assertThat(result).isEqualTo(list);
    }

    @Test
    void unidentifiedCommandTest() {
        List<String> list = List.of(MessageDatabase.unidentifiedMessage);

        List<String> result = dialogManager.sortMessage(0, "/stop");

        assertThat(result).isEqualTo(list);
    }
}
