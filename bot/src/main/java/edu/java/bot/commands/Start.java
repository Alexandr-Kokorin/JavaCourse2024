package edu.java.bot.commands;

import edu.java.bot.ScrapperClient;
import edu.java.bot.configuration.MessageDatabase;
import java.util.List;

public class Start extends Command {

    public Start(long id, String message, ScrapperClient client) {
        super(id, message, client);
    }

    @Override
    public List<String> execute() {
        var result = client.addTgChat(id, "user" + id);
        if (result.getStatusCode().is2xxSuccessful()) {
            return List.of(MessageDatabase.startMessagePart1, MessageDatabase.startMessagePart2);
        } else {
            return List.of(MessageDatabase.startMessageError);
        }
    }
}
