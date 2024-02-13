package edu.java.bot.commands;

import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Start extends Command {

    public Start(long id, String message) {
        super(id, message);
    }

    @Override
    public List<String> execute() {
        if (message.equals("/start")) {
            return commandProcessing();
        } else {
            return nameProcessing();
        }
    }

    private List<String> commandProcessing() {
        // Добавить в базу пользователя с заглушкой вместо имени и изменить сотояние диалога
        // dialogState = START
        return List.of(MessageDatabase.startMessagePart1, MessageDatabase.startMessagePart2);
    }

    private List<String> nameProcessing() {
        // Добавить в базу имя пользователя и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.startMessagePart3 + message + MessageDatabase.startMessagePart4);
    }
}
