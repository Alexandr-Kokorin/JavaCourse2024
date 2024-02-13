package edu.java.bot.commands;

import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Track extends Command {

    public Track(long id, String message) {
        super(id, message);
    }

    @Override
    public List<String> execute() {
        if (message.equals("/track")) {
            return commandProcessing();
        } else {
            return linkProcessing();
        }
    }

    private List<String> commandProcessing() {
        // Изменить сотояние диалога
        // dialogState = TRACK
        return List.of(MessageDatabase.trackMessagePart1);
    }

    private List<String> linkProcessing() {
        // Проверить правильность ссылки, правильно - дальше, нет - заглушку
        // Добавить в базу ссылку и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.trackMessagePart2);
    }
}
