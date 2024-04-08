package edu.java.scrapper.service.jooq;

import edu.java.dto.StateResponse;
import edu.java.scrapper.domain.dto.Assignment;
import edu.java.scrapper.domain.jooq.JooqAssignmentRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Objects;

public class JooqChatService implements ChatService {

    private final JooqChatRepository chatRepository;
    private final JooqLinkRepository linkRepository;
    private final JooqAssignmentRepository assignmentRepository;

    public JooqChatService(
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqAssignmentRepository assignmentRepository
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
