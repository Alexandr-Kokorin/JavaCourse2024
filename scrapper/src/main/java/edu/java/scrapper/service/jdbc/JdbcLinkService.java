package edu.java.scrapper.service.jdbc;

import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.service.LinkHandler;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.Objects;
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

    @Override
    public boolean add(long chatId, URI url) {
        var link = linkRepository.findByURL(url);
        if (Objects.isNull(link)) {
            linkRepository.add(url, linkHandler.getLastUpdate(url));
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
