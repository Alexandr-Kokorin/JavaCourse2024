package edu.java.scrapper.service.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkService implements LinkService {

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
    public boolean add(long chatId, URI url) {
        var link = linkRepository.findByURL(url);
        if (Objects.isNull(link)) {
            try {
                addLink(url);
            } catch (JsonProcessingException e) {
                return false;
            }
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

    private void addLink(URI url) throws JsonProcessingException {
        String type = linkHandler.getType(url);
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        String data = "";
        if (type.equals("github")) {
            GitHub gitHub = gitHubHandler.getInfo(url);
            lastUpdate = gitHub.repository().pushedTime();
            data = Json.mapper().writeValueAsString(gitHubHandler.getData(gitHub));
        }
        else if (type.equals("stackoverflow")) {
            Question question = stackOverflowHandler.getInfo(url);
            lastUpdate = question.items()
                .stream().max((o1, o2) -> o1.lastActivityDate().isAfter(o2.lastActivityDate()) ? 1: -1)
                .orElseThrow().lastActivityDate();
            data = Json.mapper().writeValueAsString(stackOverflowHandler.getData(question));
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
