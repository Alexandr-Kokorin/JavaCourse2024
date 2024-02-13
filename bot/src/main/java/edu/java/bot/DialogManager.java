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
import org.springframework.stereotype.Component;

@Component
public class DialogManager {

    public List<String> sortMessage(long id, String message) {
        if (message.startsWith("/")) {
            return sortCommand(id, message);
        } else {
            return sortText(id, message);
        }
    }

    private List<String> sortCommand(long id, String message) {
        // Проверка состояния на NONE, да - дальше, нет - заглушку
        Command command = switch (message) {
            case "/start" -> new Start(id, message);
            case "/help" -> new Help(id, message);
            case "/track" -> new Track(id, message);
            case "/untrack" -> new Untrack(id, message);
            case "/list" -> new ListOfLinks(id, message);
            default -> null;
        };
        if (Objects.isNull(command)) {
            return List.of(MessageDatabase.unidentifiedMessage);
        } else {
            return command.execute();
        }
    }

    private List<String> sortText(long id, String message) {
        // Достаем состояние из базы
        DialogState state = DialogState.NONE;
        Command command =  switch (state) {
            case START -> new Start(id, message);
            case TRACK -> new Track(id, message);
            case UNTRACK -> new Untrack(id, message);
            default -> null;
        };
        if (Objects.isNull(command)) {
            return List.of(MessageDatabase.gagMessage);
        } else {
            return command.execute();
        }
    }
}
