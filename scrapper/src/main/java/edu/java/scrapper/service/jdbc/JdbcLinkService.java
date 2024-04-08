package edu.java.scrapper.service.jdbc;

import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.jdbc.JdbcAssignmentRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;

public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;
    private final JdbcAssignmentRepository assignmentRepository;
    private final LinkHandler linkHandler;
    private final GitHubHandler gitHubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    public JdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcAssignmentRepository assignmentRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        this.linkRepository = linkRepository;
        this.assignmentRepository = assignmentRepository;
        this.linkHandler = linkHandler;
        this.gitHubHandler = gitHubHandler;
        this.stackOverflowHandler = stackOverflowHandler;
    }

    @Override
    public boolean add(long chatId, URI url) {
        var link = linkRepository.findByURL(url);
        if (Objects.isNull(link)) {
            addLink(url);
            link = linkRepository.findByURL(url);
            assignmentRepository.add(chatId, link.id());
            return true;
        }
        var assignment = assignmentRepository.find(chatId, link.id());
        if (Objects.isNull(assignment)) {
            assignmentRepository.add(chatId, link.id());
            return true;
        }
        return false;
    }

    private void addLink(URI url) {
        String type = linkHandler.getType(url);
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        String data = "";
        if (type.equals("github")) {
            GitHub gitHub = gitHubHandler.getInfo(url);
            lastUpdate = gitHub.repository().pushedTime();
            data = gitHubHandler.getData(gitHub);
        } else if (type.equals("stackoverflow")) {
            Question question = stackOverflowHandler.getInfo(url);
            lastUpdate = stackOverflowHandler.getLastUpdate(question);
            data = stackOverflowHandler.getData(question);
        }
        linkRepository.add(url, lastUpdate, type, data);
    }

    @Override
    public boolean remove(long chatId, URI url) {
        var link = linkRepository.findByURL(url);
        if (Objects.isNull(link)) {
            return false;
        }
        assignmentRepository.remove(chatId, link.id());
        var assignments = assignmentRepository.findAllByLinkId(link.id());
        if (assignments.isEmpty()) {
            linkRepository.remove(link.id());
        }
        return true;
    }

    @Override
    public ListLinksResponse listAll(long chatId) {
        var assignments = assignmentRepository.findAllByChatId(chatId);
        var links = new LinkResponse[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            var temp = linkRepository.findById(assignments.get(i).linkId());
            links[i] = new LinkResponse(temp.id(), temp.url());
        }
        return new ListLinksResponse(links, links.length);
    }
}
