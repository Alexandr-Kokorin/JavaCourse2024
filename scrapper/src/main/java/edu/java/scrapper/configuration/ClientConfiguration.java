package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient();
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient();
    }
}
