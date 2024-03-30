package edu.java.scrapper.service.jdbc;

import edu.java.dto.StateResponse;
import edu.java.scrapper.domain.dto.Assignment;
import edu.java.scrapper.domain.jdbc.JdbcAssignmentRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Objects;

public class JdbcChatService implements ChatService {

    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcAssignmentRepository assignmentRepository;

    public JdbcChatService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcAssignmentRepository assignmentRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.assignmentRepository = assignmentRepository;
    }

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
        var chat = chatRepository.find(chatId);
        if (Objects.isNull(chat)) {
            return new StateResponse("NONE");
        }
        return new StateResponse(chat.state());
    }

    @Override
    public void setState(long chatId, String state) {
        chatRepository.updateState(chatId, state);
    }
}
