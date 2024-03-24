package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
}
