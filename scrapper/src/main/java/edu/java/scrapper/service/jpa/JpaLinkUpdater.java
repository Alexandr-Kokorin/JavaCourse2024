package edu.java.scrapper.service.jpa;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.domain.data.GitHubData;
import edu.java.scrapper.domain.data.StackOverflowData;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.entity.LinkEntity;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JpaLinkUpdater implements LinkUpdater {

    private final JpaLinkRepository jpaLinkRepository;
    private final LinkHandler linkHandler;
    private final GitHubHandler gitHubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    public JpaLinkUpdater(
        JpaLinkRepository jpaLinkRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.linkHandler = linkHandler;
        this.gitHubHandler = gitHubHandler;
        this.stackOverflowHandler = stackOverflowHandler;
    }

    @Override
    public List<Link> getLinks(int count) {
        var linksEntity = jpaLinkRepository.findAllWithLimit(count);
        var links = new ArrayList<Link>();
        for (LinkEntity link : linksEntity) {
            links.add(new Link(link.getId(), URI.create(link.getUrl()), link.getType(),
                link.getData(), link.getLastUpdate(), link.getLastCheck()));
        }
        return links;
    }

    @Override
    public LinkUpdate update(Link link) {
        var linkEntity = jpaLinkRepository.findById(link.id()).orElseThrow();
        linkEntity.setLastCheck(OffsetDateTime.now());
        jpaLinkRepository.saveAndFlush(linkEntity);
        var type = linkHandler.getType(link.url());
        String[] description = switch (type) {
            case "github" -> updateGitHub(link);
            case "stackoverflow" -> updateStackOverflow(link);
            default -> null;
        };
        if (Objects.nonNull(description)) {
            var chats = jpaLinkRepository.findById(link.id()).orElseThrow().getChats();
            var tgChatIds = new long[chats.size()];
            for (int i = 0; i < chats.size(); i++) {
                tgChatIds[i] = chats.get(i).getId();
            }
            return new LinkUpdate(link.url(), description, tgChatIds);
        }
        return null;
    }

    private String[] updateGitHub(Link link) {
        var gitHub = gitHubHandler.getInfo(link.url());
        var lastUpdate = gitHub.repository().pushedTime();
        if (link.lastUpdate().isBefore(lastUpdate)) {
            GitHubData data = gitHubHandler.getGitHubData(link);
            List<String> description = gitHubHandler.getDescriptionNewCommit(data, gitHub, link);
            description.addAll(gitHubHandler.getDescriptionCreateOrDeletePull(data, gitHub, link));
            description.addAll(gitHubHandler.getDescriptionCreateOrDeleteBranch(data, gitHub));
            description.addAll(linkHandler.getDescriptionUnknownUpdate(description));
            var linkEntity = jpaLinkRepository.findById(link.id()).orElseThrow();
            linkEntity.setLastUpdate(lastUpdate);
            linkEntity.setData(gitHubHandler.getData(gitHub));
            jpaLinkRepository.saveAndFlush(linkEntity);
            return description.toArray(new String[0]);
        }
        return null;
    }

    private String[] updateStackOverflow(Link link) {
        var question = stackOverflowHandler.getInfo(link.url());
        var lastUpdate = stackOverflowHandler.getLastUpdate(question);
        if (link.lastUpdate().isBefore(lastUpdate)) {
            StackOverflowData data = stackOverflowHandler.getStackOverflowData(link);
            List<String> description = stackOverflowHandler.getDescriptionNewAnswer(data, question, link);
            description.addAll(stackOverflowHandler.getDescriptionUpdateAnswer(question, link));
            description.addAll(linkHandler.getDescriptionUnknownUpdate(description));
            var linkEntity = jpaLinkRepository.findById(link.id()).orElseThrow();
            linkEntity.setLastUpdate(lastUpdate);
            linkEntity.setData(stackOverflowHandler.getData(question));
            jpaLinkRepository.saveAndFlush(linkEntity);
            return description.toArray(new String[0]);
        }
        return null;
    }
}
