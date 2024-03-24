package edu.java.scrapper.service.jpa;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkUpdater;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JpaLinkUpdater implements LinkUpdater {

    @Override
    public List<Link> getLinks(int count) {
        return null;
    }

    @Override
    public LinkUpdate update(Link link) {
        return null;
    }
}
