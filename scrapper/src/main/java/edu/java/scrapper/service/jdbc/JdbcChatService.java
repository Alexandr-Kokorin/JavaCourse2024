package edu.java.scrapper.service.jdbc;

import edu.java.dto.StateResponse;
import edu.java.scrapper.domain.AssignmentRepository;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Assignment;
import edu.java.scrapper.service.ChatService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {

    @Autowired
    @Qualifier("jdbcChatRepository")
    private ChatRepository chatRepository;
    @Autowired
    @Qualifier("jdbcLinkRepository")
    private LinkRepository linkRepository;
    @Autowired
    @Qualifier("jdbcAssignmentRepository")
    private AssignmentRepository assignmentRepository;

    @Override
    public boolean add(long chatId, String name) {
        if (Objects.nonNull(chatRepository.find(chatId))) {
            return false;
        }
        chatRepository.add(chatId, name);
        return true;
    }

    @Override
    public boolean remove(long chatId) {
        if (Objects.isNull(chatRepository.find(chatId))) {
            return false;
        }
        var assignments = assignmentRepository.findAllByChatId(chatId);
        chatRepository.remove(chatId);
        for (Assignment assignment: assignments) {
            if (assignmentRepository.findAllByLinkId(assignment.linkId()).isEmpty()) {
                linkRepository.remove(assignment.linkId());
            }
        }
        return true;
    }

    @Override
    public StateResponse getState(long chatId) {
        return new StateResponse(chatRepository.find(chatId).state());
    }

    @Override
    public void setState(long chatId, String state) {
        chatRepository.updateState(chatId, state);
    }
}
