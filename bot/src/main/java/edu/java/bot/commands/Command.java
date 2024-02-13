package edu.java.bot.commands;

import java.util.List;

public abstract class Command {

    long id;
    String message;

    public Command(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public abstract List<String> execute();
}
