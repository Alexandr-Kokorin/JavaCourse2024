package edu.java.bot.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.bot.ScrapperClient;
import edu.java.bot.configuration.MessageDatabase;
import edu.java.bot.states.DialogState;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class Untrack extends Command {

    public Untrack(long id, String message, ScrapperClient client) {
        super(id, message, client);
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
        client.updateState(id, DialogState.UNTRACK);
        return List.of(MessageDatabase.untrackMessagePart1);
    }

    private List<String> linkProcessing() {
        client.updateState(id, DialogState.NONE);
        ResponseEntity<Void> result;
        try {
            result = client.deleteLink(id, URI.create(message));
        } catch (JsonProcessingException e) {
            return List.of(MessageDatabase.untrackMessageError);
        }
        if (!result.getStatusCode().is2xxSuccessful()) {
            return List.of(MessageDatabase.untrackMessageError);
        }
        return List.of(MessageDatabase.untrackMessagePart2);
    }
}
