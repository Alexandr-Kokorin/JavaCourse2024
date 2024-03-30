package edu.java.bot.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.bot.ScrapperClient;
import edu.java.bot.configuration.MessageDatabase;
import edu.java.bot.states.DialogState;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class Track extends Command {

    private static final String GITHUB = "^https://github.com/[^/]*/[^/]*$";
    private static final String STACKOVERFLOW = "^https://stackoverflow.com/questions/\\d*/[^/]*$";

    public Track(long id, String message, ScrapperClient client) {
        super(id, message, client);
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
        client.updateState(id, DialogState.TRACK);
        return List.of(MessageDatabase.trackMessagePart1);
    }

    private List<String> linkProcessing() {
        client.updateState(id, DialogState.NONE);
        if (!message.matches(GITHUB) && !message.matches(STACKOVERFLOW)) {
            return List.of(MessageDatabase.trackMessageError1);
        }
        ResponseEntity<Void> result;
        try {
            result = client.addLink(id, URI.create(message));
        } catch (JsonProcessingException e) {
            return List.of(MessageDatabase.trackMessageError1);
        }
        if (!result.getStatusCode().is2xxSuccessful()) {
            return List.of(MessageDatabase.trackMessageError2);
        }
        return List.of(MessageDatabase.trackMessagePart2);
    }
}
