package edu.java.scrapper.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.dto.Link;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("MultipleStringLiterals")
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

    public StackOverflowData getStackOverflowData(Link link) {
        try {
            return Json.mapper().readValue(link.data(), StackOverflowData.class);
        } catch (JsonProcessingException e) {
            return new StackOverflowData(0);
        }
    }

    public List<String> getDescriptionNewAnswer(StackOverflowData data, Question question, Link link) {
        List<String> description = new ArrayList<>();
        if (data.numberOfAnswers() != question.items().size()) {
            for (Question.Item item : question.items()) {
                if (item.creationDate().isAfter(link.lastUpdate())) {
                    description.add("Пользователь " + item.owner().name() + " добавил новый ответ.");
                }
            }
        }
        return description;
    }

    public List<String> getDescriptionUpdateAnswer(Question question, Link link) {
        List<String> description = new ArrayList<>();
        for (Question.Item item : question.items()) {
            if (item.lastActivityDate().isAfter(link.lastUpdate())) {
                description.add("Пользователь " + item.owner().name() + " обновил свой ответ.");
            }
        }
        return description;
    }
}
