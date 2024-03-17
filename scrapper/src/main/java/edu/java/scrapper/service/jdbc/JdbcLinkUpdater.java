package edu.java.scrapper.service.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.dto.LinkUpdate;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import io.swagger.v3.core.util.Json;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@SuppressWarnings("MultipleStringLiterals")
@Service
public class JdbcLinkUpdater implements LinkUpdater {

    @Autowired
    @Qualifier("jdbcLinkRepository")
    private LinkRepository linkRepository;
    @Autowired
    @Qualifier("jdbcAssignmentRepository")
    private AssignmentRepository assignmentRepository;
    @Autowired
    private LinkHandler linkHandler;
    @Autowired
    private GitHubHandler gitHubHandler;
    @Autowired
    private StackOverflowHandler stackOverflowHandler;

    @Override
    public List<Link> getLinks(int count) {
        return linkRepository.findAllWithLimit(count);
    }

    @Override
    public LinkUpdate update(Link link) {
        linkRepository.updateTimeOfLastCheck(link.id(), OffsetDateTime.now());
        var type = linkHandler.getType(link.url());
        String[] description = switch (type) {
            case "github" -> updateGitHub(link);
            case "stackoverflow" -> updateStackOverflow(link);
            default -> null;
        };
        if (Objects.nonNull(description)) {
            var assignments = assignmentRepository.findAllByLinkId(link.id());
            var tgChatIds = new long[assignments.size()];
            for (int i = 0; i < assignments.size(); i++) {
                tgChatIds[i] = assignments.get(i).chatId();
            }
            return new LinkUpdate(link.url(), description, tgChatIds);
        }
        return null;
    }

    private String[] updateGitHub(Link link) {
        return null;
    }

    private String[] updateStackOverflow(Link link) {
        var question = stackOverflowHandler.getInfo(link.url());
        var lastUpdate = stackOverflowHandler.getLastUpdate(question);
        if (link.lastUpdate().isBefore(lastUpdate)) {
            linkRepository.updateTimeOfLastUpdate(link.id(), lastUpdate);
            StackOverflowData data = getStackOverflowData(link);
            List<String> description = getDescriptionNewAnswer(data, question, link);
            description.addAll(getDescriptionUpdateAnswer(question, link));
            return description.toArray(new String[0]);
        }
        return null;
    }

    private StackOverflowData getStackOverflowData(Link link) {
        try {
            return Json.mapper().readValue(link.data(), StackOverflowData.class);
        } catch (JsonProcessingException e) {
            return new StackOverflowData(0);
        }
    }

    private List<String> getDescriptionNewAnswer(StackOverflowData data, Question question, Link link) {
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

    private List<String> getDescriptionUpdateAnswer(Question question, Link link) {
        List<String> description = new ArrayList<>();
        for (Question.Item item : question.items()) {
            if (item.lastActivityDate().isAfter(link.lastUpdate())) {
                description.add("Пользователь " + item.owner().name() + " обновил свой ответ.");
            }
        }
        return description;
    }
}
