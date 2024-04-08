package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jdbc.JdbcAssignmentRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.handlers.GitHubHandler;
import edu.java.scrapper.service.handlers.LinkHandler;
import edu.java.scrapper.service.handlers.StackOverflowHandler;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public ChatService chatService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcAssignmentRepository assignmentRepository
    ) {
        return new JdbcChatService(chatRepository, linkRepository, assignmentRepository);
    }

    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcAssignmentRepository assignmentRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JdbcLinkService(linkRepository, assignmentRepository,
            linkHandler, gitHubHandler, stackOverflowHandler);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepository linkRepository,
        JdbcAssignmentRepository assignmentRepository,
        LinkHandler linkHandler,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        return new JdbcLinkUpdater(linkRepository, assignmentRepository,
            linkHandler, gitHubHandler, stackOverflowHandler);
    }
}
