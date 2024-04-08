package edu.java.bot.commands;

import edu.java.bot.ScrapperClient;
import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Help extends Command {

    public Help(long id, String message, ScrapperClient client) {
        super(id, message, client);
    }

    @Override
    public List<String> execute() {
        return List.of(MessageDatabase.helpMessage);
    }
}
