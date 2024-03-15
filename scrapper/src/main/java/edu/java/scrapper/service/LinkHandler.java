package edu.java.scrapper.service;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.stackoverflowDTO.Item;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkHandler {

    @Autowired
    private GitHubClient gitHubClient;
    @Autowired
    private StackOverflowClient stackOverflowClient;

    public OffsetDateTime getLastUpdate(URI url) {
        OffsetDateTime time = OffsetDateTime.now();
        if (url.toString().contains("github.com")) {
            time = githubLastUpdate(url);
        }
        if (url.toString().contains("stackoverflow.com")) {
            time = stackoverflowLastUpdate(url);
        }
        return time;
    }

    private OffsetDateTime githubLastUpdate(URI url) {
        var parts = url.toString().split("/");
        var repository = gitHubClient.getRepositoryInfo(parts[parts.length - 2], parts[parts.length - 1]).block();
        return repository.updatedTime();
    }

    private OffsetDateTime stackoverflowLastUpdate(URI url) {
        var parts = url.toString().split("/");
        var items = stackOverflowClient.getQuestionInfo(Integer.parseInt(parts[parts.length - 2])).block().items();
        var lastUpdate = items.get(0).lastActivityDate();
        for (Item item : items) {
            if (item.lastActivityDate().isAfter(lastUpdate)) {
                lastUpdate = item.lastActivityDate();
            }
        }
        return lastUpdate;
    }
}
