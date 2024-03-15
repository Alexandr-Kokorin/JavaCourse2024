package edu.java.scrapper.service.jdbc;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkHandler;
import edu.java.scrapper.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
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

    @Override
    public List<Link> getLinks(int count) {
        return linkRepository.findAllWithLimit(count);
    }

    @Override
    public LinkUpdate update(Link link) {
        var lastUpdate = linkHandler.getLastUpdate(link.url());
        if (link.lastUpdate().isBefore(lastUpdate)) {
            linkRepository.updateTimeOfLastUpdate(link.id(), lastUpdate);
            linkRepository.updateTimeOfLastCheck(link.id(), OffsetDateTime.now());
            var assignments = assignmentRepository.findAllByLinkId(link.id());
            var tgChatIds = new long[assignments.size()];
            for (int i = 0; i < assignments.size(); i++) {
                tgChatIds[i] = assignments.get(i).chatId();
            }
            return new LinkUpdate(link.url(), "Ресурс был обновлён.", tgChatIds);
        }
        linkRepository.updateTimeOfLastCheck(link.id(), OffsetDateTime.now());
        return null;
    }
}
