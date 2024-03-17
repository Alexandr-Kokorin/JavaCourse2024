package edu.java.scrapper.service.jdbc;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        var lastUpdate = question.items()
            .stream().max((o1, o2) -> o1.lastActivityDate().isAfter(o2.lastActivityDate()) ? 1: -1)
            .orElseThrow().lastActivityDate();
        if (link.lastUpdate().isBefore(lastUpdate)) {
            linkRepository.updateTimeOfLastUpdate(link.id(), lastUpdate);
        }
        return null;
    }
}