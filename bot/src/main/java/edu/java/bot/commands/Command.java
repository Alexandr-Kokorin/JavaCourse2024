package edu.java.bot.commands;

import edu.java.bot.ScrapperClient;
import java.util.List;

public abstract class Command {

    long id;
    String message;
    ScrapperClient client;

    public Command(long id, String message, ScrapperClient client) {
        this.id = id;
        this.message = message;
        this.client = client;
    }

    public abstract List<String> execute();
}
