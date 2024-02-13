package edu.java.bot.commands;

import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class ListOfLinks extends Command {

    public ListOfLinks(long id, String message) {
        super(id, message);
    }

    @Override
    public List<String> execute() {
        // Запрашиваем список из базы и выводим либо список, либо заглушку
        return List.of(MessageDatabase.emptyListMessage);
    }
}
