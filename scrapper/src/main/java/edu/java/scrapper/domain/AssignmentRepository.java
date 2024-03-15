package edu.java.scrapper.domain;

import edu.java.scrapper.domain.dto.Assignment;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface AssignmentRepository {

    void add(long chatId, long linkId);

    void remove(long chatId, long linkId);

    Assignment find(long chatId, long linkId);

    List<Assignment> findAllByChatId(long chatId);

    List<Assignment> findAllByLinkId(long linkId);
}
