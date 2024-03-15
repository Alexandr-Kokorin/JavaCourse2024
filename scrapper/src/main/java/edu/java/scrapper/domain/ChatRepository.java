package edu.java.scrapper.domain;

import edu.java.scrapper.domain.dto.Chat;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository {

    void add(long id, String name);

    void remove(long id);

    void updateState(long id, String state);

    Chat find(long id);
}
