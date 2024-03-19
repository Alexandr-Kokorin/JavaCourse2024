package edu.java.scrapper.service.jooq;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.data.GitHubData;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JooqLinkUpdater implements LinkUpdater {

    @Autowired
    @Qualifier("jooqLinkRepository")
    private LinkRepository linkRepository;
    @Autowired
    @Qualifier("jooqAssignmentRepository")
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
        var gitHub = gitHubHandler.getInfo(link.url());
        var lastUpdate = gitHub.repository().pushedTime();
        if (link.lastUpdate().isBefore(lastUpdate)) {
            GitHubData data = gitHubHandler.getGitHubData(link);
            List<String> description = gitHubHandler.getDescriptionNewCommit(data, gitHub, link);
            description.addAll(gitHubHandler.getDescriptionCreateOrDeletePull(data, gitHub, link));
            description.addAll(gitHubHandler.getDescriptionCreateOrDeleteBranch(data, gitHub));
            description.addAll(linkHandler.getDescriptionUnknownUpdate(description));
            linkRepository.updateTimeOfLastUpdate(link.id(), lastUpdate);
            linkRepository.updateData(link.id(), gitHubHandler.getData(gitHub));
            return description.toArray(new String[0]);
        }
        return null;
    }

    private String[] updateStackOverflow(Link link) {
        var question = stackOverflowHandler.getInfo(link.url());
        var lastUpdate = stackOverflowHandler.getLastUpdate(question);
        if (link.lastUpdate().isBefore(lastUpdate)) {
            StackOverflowData data = stackOverflowHandler.getStackOverflowData(link);
            List<String> description = stackOverflowHandler.getDescriptionNewAnswer(data, question, link);
            description.addAll(stackOverflowHandler.getDescriptionUpdateAnswer(question, link));
            description.addAll(linkHandler.getDescriptionUnknownUpdate(description));
            linkRepository.updateTimeOfLastUpdate(link.id(), lastUpdate);
            linkRepository.updateData(link.id(), stackOverflowHandler.getData(question));
            return description.toArray(new String[0]);
        }
        return null;
    }
}
