package edu.java.bot;

import edu.java.bot.configuration.MessageDatabase;
import edu.java.bot.states.DialogState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DialogManager {

    public List<String> sortMessage(long id, String text) {
        if (text.startsWith("/")) {
            return sortCommand(id, text);
        } else {
            return sortText(id, text);
        }
    }

    private List<String> sortCommand(long id, String text) {
        // Проверка состояния на NONE, да - дальше, нет - заглушку
        return switch (text) {
            case "/start" -> startCommandProcessingPart1(id);
            case "/help" -> helpCommandProcessing(id);
            case "/track" -> trackCommandProcessingPart1(id);
            case "/untrack" -> untrackCommandProcessingPart1(id);
            case "/list" -> listCommandProcessing(id);
            default -> List.of(MessageDatabase.unidentifiedMessage);
        };
    }

    private List<String> startCommandProcessingPart1(long id) {
        // Добавить в базу пользователя с заглушками и изменить сотояние диалога
        // dialogState = START
        return List.of(MessageDatabase.startMessagePart1, MessageDatabase.startMessagePart2);
    }

    private List<String> trackCommandProcessingPart1(long id) {
        // dialogState = TRACK
        return List.of(MessageDatabase.trackMessagePart1);
    }

    private List<String> untrackCommandProcessingPart1(long id) {
        // dialogState = UNTRACK
        return List.of(MessageDatabase.untrackMessagePart1);
    }

    private List<String> helpCommandProcessing(long id) {
        return List.of(MessageDatabase.helpMessage);
    }

    private List<String> listCommandProcessing(long id) {
        // Запрашиваем список из базы и выводим либо список, либо заглушку
        return List.of(MessageDatabase.emptyListMessage);
    }

    private List<String> sortText(long id, String text) {
        // Достаем состояние из базы
        DialogState state = DialogState.NONE;
        return switch (state) {
            case START -> startCommandProcessingPart2(id, text);
            case TRACK -> trackCommandProcessingPart2(id, text);
            case UNTRACK -> untrackCommandProcessingPart2(id, text);
            default -> List.of(MessageDatabase.gagMessage);
        };
    }

    private List<String> startCommandProcessingPart2(long id, String text) {
        // Добавить в базу имя пользователя и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.startMessagePart3 + text + MessageDatabase.startMessagePart4);
    }

    private List<String> trackCommandProcessingPart2(long id, String text) {
        // Проверить правильность ссылки, правильно - дальше, нет заглушку
        // Добавить в базу ссылку и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.trackMessagePart2);
    }

    private List<String> untrackCommandProcessingPart2(long id, String text) {
        // Проверить наличие ссылки, правильно - дальше, нет заглушку
        // Удалить из базы ссылку и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.untrackMessagePart2);
    }
}
