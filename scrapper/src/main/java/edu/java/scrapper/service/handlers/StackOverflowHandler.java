package edu.java.scrapper.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.data.StackOverflowData;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowHandler {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    public Question getInfo(URI url) {
        var parts = url.toString().split("/");
        return stackOverflowClient.getInfo(Integer.parseInt(parts[parts.length - 2]));
    }

    public String getData(Question question) {
        try {
            return Json.mapper().writeValueAsString(new StackOverflowData(question.items().size()));
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public OffsetDateTime getLastUpdate(Question question) {
        if (question.items().size() == 0) {
            return OffsetDateTime.now();
        }
        var lastUpdate = question.items().get(0).lastActivityDate();
        for (Question.Item item : question.items()) {
            if (item.lastActivityDate().isAfter(lastUpdate)) {
                lastUpdate = item.lastActivityDate();
            }
        }
        return lastUpdate;
    }
}
