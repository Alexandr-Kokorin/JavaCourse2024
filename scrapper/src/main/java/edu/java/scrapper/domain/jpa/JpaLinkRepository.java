package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    LinkEntity findByUrl(String url);
}
