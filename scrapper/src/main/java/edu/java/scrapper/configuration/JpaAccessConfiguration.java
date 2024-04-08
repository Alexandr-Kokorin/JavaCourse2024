package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import edu.java.scrapper.service.jpa.JpaLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public ChatService chatService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository
    ) {
        return new JpaChatService(chatRepository, linkRepository);
    }

    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JpaLinkService(linkRepository, chatRepository,
            linkHandler, gitHubHandler, stackOverflowHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JpaLinkRepository linkRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JpaLinkUpdater(linkRepository, linkHandler,
            gitHubHandler, stackOverflowHandler);
    }
}
