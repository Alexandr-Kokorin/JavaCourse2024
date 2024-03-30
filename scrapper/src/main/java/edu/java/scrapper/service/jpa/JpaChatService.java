package edu.java.scrapper.service.jpa;

import edu.java.dto.StateResponse;
import edu.java.scrapper.domain.entity.ChatEntity;
import edu.java.scrapper.domain.entity.LinkEntity;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.time.OffsetDateTime;

@SuppressWarnings("MultipleStringLiterals")
public class JpaChatService implements ChatService {

    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    public JpaChatService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    public boolean add(long chatId, String name) {
        if (jpaChatRepository.existsById(chatId)) {
            return false;
        }
        jpaChatRepository.saveAndFlush(new ChatEntity(chatId, name, "NONE", OffsetDateTime.now()));
        return true;
    }

    @Override
    public boolean remove(long chatId) {
        if (!jpaChatRepository.existsById(chatId)) {
            return false;
        }
        var links = jpaChatRepository.findById(chatId).orElseThrow().getLinks();
        for (LinkEntity link : links) {
            if (link.getChats().size() == 1) {
                jpaLinkRepository.deleteById(link.getId());
            }
        }
        jpaChatRepository.deleteById(chatId);
        jpaLinkRepository.flush();
        jpaChatRepository.flush();
        return true;
    }

    @Override
    public StateResponse getState(long chatId) {
        if (!jpaChatRepository.existsById(chatId)) {
            return new StateResponse("NONE");
        }
        return new StateResponse(jpaChatRepository.findById(chatId).orElseThrow().getState());
    }

    @Override
    public void setState(long chatId, String state) {
        var chat = jpaChatRepository.findById(chatId).orElseThrow();
        chat.setState(state);
        jpaChatRepository.saveAndFlush(chat);
    }
}
