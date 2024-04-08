package edu.java.scrapper.service;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.dto.Link;
import java.util.List;

public interface LinkUpdater {

    List<Link> getLinks(int count);

    LinkUpdate update(Link link);
}
