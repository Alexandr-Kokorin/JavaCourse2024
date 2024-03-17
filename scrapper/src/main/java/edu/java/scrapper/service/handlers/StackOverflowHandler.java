package edu.java.scrapper.service.handlers;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.data.StackOverflowData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;

@Component
public class StackOverflowHandler {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    public Question getInfo(URI url) {
        var parts = url.toString().split("/");
        return stackOverflowClient.getInfo(Integer.parseInt(parts[parts.length - 2]));
    }

    public StackOverflowData getData(Question question) {
        return new StackOverflowData(question.items().size());
    }
}
