package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.entity.LinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    LinkEntity findByUrl(String url);

    @Query(value = "SELECT * FROM link ORDER BY last_check LIMIT ?", nativeQuery = true)
    List<LinkEntity> findAllWithLimit(int count);
}
