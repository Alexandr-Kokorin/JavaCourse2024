package edu.java.bot.commands;

import edu.java.bot.ScrapperClient;
import edu.java.bot.configuration.MessageDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListOfLinks extends Command {

    public ListOfLinks(long id, String message, ScrapperClient client) {
        super(id, message, client);
    }

    @Override
    public List<String> execute() {
        var list = client.getLinks(id).getBody();
        if (Objects.isNull(list) || list.size() == 0) {
            return List.of(MessageDatabase.emptyListMessage);
        }
        var result = new ArrayList<String>();
        result.add(MessageDatabase.listMessage);
        for (int i = 0; i < list.size(); i++) {
            result.add((i + 1) + ") " + list.links()[i].url().toString() + "\n");
        }
        return result;
    }
}
