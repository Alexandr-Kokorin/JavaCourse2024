package edu.java.bot;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.Help;
import edu.java.bot.commands.ListOfLinks;
import edu.java.bot.commands.Start;
import edu.java.bot.commands.Track;
import edu.java.bot.commands.Untrack;
import edu.java.bot.configuration.MessageDatabase;
import edu.java.bot.states.DialogState;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DialogManager {

    @Autowired
    private ScrapperClient scrapperClient;

    public List<String> sortMessage(long id, String message) {
        if (message.startsWith("/")) {
            return sortCommand(id, message);
        } else {
            return sortText(id, message);
        }
    }

    private List<String> sortCommand(long id, String message) {
        var response = scrapperClient.getState(id);
        if (response.getStatusCode().is5xxServerError()) {
            return List.of(MessageDatabase.errorMessage);
        }
        DialogState state = DialogState.valueOf(response.getBody().state());
        if (state != DialogState.NONE) {
            return List.of(MessageDatabase.unidentifiedMessage);
        }
        Command command = switch (message) {
            case "/start" -> new Start(id, message, scrapperClient);
            case "/help" -> new Help(id, message, scrapperClient);
            case "/track" -> new Track(id, message, scrapperClient);
            case "/untrack" -> new Untrack(id, message, scrapperClient);
            case "/list" -> new ListOfLinks(id, message, scrapperClient);
            default -> null;
        };
        if (Objects.isNull(command)) {
            return List.of(MessageDatabase.unidentifiedMessage);
        } else {
            return command.execute();
        }
    }

    private List<String> sortText(long id, String message) {
        var response = scrapperClient.getState(id);
        if (response.getStatusCode().is5xxServerError()) {
            return List.of(MessageDatabase.errorMessage);
        }
        DialogState state = DialogState.valueOf(response.getBody().state());
        Command command = switch (state) {
            case TRACK -> new Track(id, message, scrapperClient);
            case UNTRACK -> new Untrack(id, message, scrapperClient);
            default -> null;
        };
        if (Objects.isNull(command)) {
            return List.of(MessageDatabase.unidentifiedMessage);
        } else {
            return command.execute();
        }
    }
}
