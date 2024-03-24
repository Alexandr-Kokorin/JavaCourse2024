package edu.java.scrapper.service.jpa;

import edu.java.dto.StateResponse;
import edu.java.scrapper.domain.entity.ChatEntity;
import edu.java.scrapper.domain.entity.LinkEntity;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaChatService implements ChatService {

    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

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
        jpaChatRepository.deleteById(chatId);
        jpaChatRepository.flush();
        for (LinkEntity link : links) {
            if (link.getChats().isEmpty()) {
                jpaLinkRepository.deleteById(link.getId());
            }
        }
        jpaLinkRepository.flush();
        return true;
    }

    @Override
    public StateResponse getState(long chatId) {
        return new StateResponse(jpaChatRepository.findById(chatId).orElseThrow().getState());
    }

    @Override
    public void setState(long chatId, String state) {
        var chat = jpaChatRepository.findById(chatId);
        chat.orElseThrow().setState(state);
        jpaChatRepository.flush();
    }
}
