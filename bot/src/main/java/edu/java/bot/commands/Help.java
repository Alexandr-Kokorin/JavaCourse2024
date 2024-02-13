package edu.java.bot.commands;

import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Help extends Command {

    public Help(long id, String message) {
        super(id, message);
    }

    @Override
    public List<String> execute() {
        return List.of(MessageDatabase.helpMessage);
    }
}
