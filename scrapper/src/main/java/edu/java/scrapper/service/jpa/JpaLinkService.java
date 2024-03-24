package edu.java.scrapper.service.jpa;

import edu.java.dto.ListLinksResponse;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.clients.stackoverflowDTO.Question;
import edu.java.scrapper.domain.entity.ChatEntity;
import edu.java.scrapper.domain.entity.LinkEntity;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaLinkService implements LinkService {

    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private LinkHandler linkHandler;
    @Autowired
    private GitHubHandler gitHubHandler;
    @Autowired
    private StackOverflowHandler stackOverflowHandler;

    @Override
    public boolean add(long chatId, URI url) {
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (Objects.isNull(link)) {
            addLink(chatId, url);
            return true;
        }
        var chats = link.getChats();
        var chat = jpaChatRepository.findById(chatId).orElseThrow();
        if (!chats.contains(chat)) {
            link.getChats().add(chat);
            jpaLinkRepository.flush();
            return true;
        }
        return false;
    }

    private void addLink(long chatId, URI url) {
        String type = linkHandler.getType(url);
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        String data = "";
        if (type.equals("github")) {
            GitHub gitHub = gitHubHandler.getInfo(url);
            lastUpdate = gitHub.repository().pushedTime();
            data = gitHubHandler.getData(gitHub);
        } else if (type.equals("stackoverflow")) {
            Question question = stackOverflowHandler.getInfo(url);
            lastUpdate = stackOverflowHandler.getLastUpdate(question);
            data = stackOverflowHandler.getData(question);
        }
        var chat = jpaChatRepository.findById(chatId).orElse(new ChatEntity());
        jpaLinkRepository.saveAndFlush(
            new LinkEntity(url.toString(), type, data, lastUpdate, OffsetDateTime.now(), Set.of(chat))
        );
    }

    @Override
    public boolean remove(long chatId, URI url) {
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (Objects.isNull(link)) {
            return false;
        }
        return true;
    }

    @Override
    public ListLinksResponse listAll(long chatId) {
        return null;
    }
}
