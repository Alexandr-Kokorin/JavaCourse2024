package edu.java.bot.commands;

import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Untrack extends Command {

    public Untrack(long id, String message) {
        super(id, message);
    }

    @Override
    public List<String> execute() {
        if (message.equals("/untrack")) {
            return commandProcessing();
        } else {
            return linkProcessing();
        }
    }

    private List<String> commandProcessing() {
        // Изменить сотояние диалога
        // dialogState = UNTRACK
        return List.of(MessageDatabase.untrackMessagePart1);
    }

    private List<String> linkProcessing() {
        // Проверить наличие ссылки, есть - дальше, нет - заглушку
        // Удалить из базы ссылку и изменить сотояние диалога
        // dialogState = NONE
        return List.of(MessageDatabase.untrackMessagePart2);
    }
}
