package edu.java.scrapper.service.jpa;

import edu.java.dto.LinkResponse;
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
import java.util.ArrayList;
import java.util.Objects;

public class JpaLinkService implements LinkService {

    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;
    private final LinkHandler linkHandler;
    private final GitHubHandler gitHubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    public JpaLinkService(
        JpaLinkRepository jpaLinkRepository,
        JpaChatRepository jpaChatRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaChatRepository = jpaChatRepository;
        this.linkHandler = linkHandler;
        this.gitHubHandler = gitHubHandler;
        this.stackOverflowHandler = stackOverflowHandler;
    }

    @Override
    public boolean add(long chatId, URI url) {
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (Objects.isNull(link)) {
            addLink(chatId, url);
            jpaChatRepository.flush();
            return true;
        }
        var chats = link.getChats();
        var chat = jpaChatRepository.findById(chatId).orElseThrow();
        if (!chats.contains(chat)) {
            chats.add(chat);
            jpaLinkRepository.saveAndFlush(link);
            jpaChatRepository.flush();
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
        var list = new ArrayList<ChatEntity>();
        list.add(jpaChatRepository.findById(chatId).orElse(new ChatEntity()));
        jpaLinkRepository.saveAndFlush(
            new LinkEntity(url.toString(), type, data, lastUpdate, OffsetDateTime.now(), list)
        );
    }

    @Override
    public boolean remove(long chatId, URI url) {
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (Objects.isNull(link)) {
            return false;
        }
        if (link.getChats().size() == 1) {
            jpaLinkRepository.deleteById(link.getId());
        } else {
            var chat = jpaChatRepository.findById(chatId).orElseThrow();
            link.getChats().remove(chat);
            jpaLinkRepository.saveAndFlush(link);
        }
        jpaLinkRepository.flush();
        jpaChatRepository.flush();
        return true;
    }

    @Override
    public ListLinksResponse listAll(long chatId) {
        var links = jpaChatRepository.findById(chatId).orElseThrow().getLinks();
        var linksResponse = new LinkResponse[links.size()];
        for (int i = 0; i < links.size(); i++) {
            linksResponse[i] = new LinkResponse(links.get(i).getId(), URI.create(links.get(i).getUrl()));
        }
        return new ListLinksResponse(linksResponse, links.size());
    }
}
