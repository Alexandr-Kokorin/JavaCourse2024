package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jooq.JooqAssignmentRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import edu.java.scrapper.service.jooq.JooqChatService;
import edu.java.scrapper.service.jooq.JooqLinkService;
import edu.java.scrapper.service.jooq.JooqLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public ChatService chatService(
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqAssignmentRepository assignmentRepository
    ) {
        return new JooqChatService(chatRepository, linkRepository, assignmentRepository);
    }

    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqAssignmentRepository assignmentRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JooqLinkService(linkRepository, assignmentRepository,
            linkHandler, gitHubHandler, stackOverflowHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JooqLinkRepository linkRepository,
        JooqAssignmentRepository assignmentRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JooqLinkUpdater(linkRepository, assignmentRepository,
            linkHandler, gitHubHandler, stackOverflowHandler);
    }
}
