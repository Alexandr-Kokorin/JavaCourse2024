package edu.java.scrapper.domain;

import edu.java.scrapper.domain.dto.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository {

    void add(URI url, OffsetDateTime lastUpdate);

    void remove(long id);

    void updateTimeOfLastUpdate(long id, OffsetDateTime lastUpdate);

    void updateTimeOfLastCheck(long id, OffsetDateTime lastCheck);

    Link findById(long id);

    Link findByURL(URI url);

    List<Link> findAllWithLimit(int count);
}
