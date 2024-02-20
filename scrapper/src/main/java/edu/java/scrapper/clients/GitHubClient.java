package edu.java.scrapper.clients;

import edu.java.scrapper.clients.githubDTO.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient() {
        this.webClient = WebClient.builder().baseUrl("https://api.GitHub.com").build();
    }

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<Repository> getRepositoryInfo(String username, String name) {
        return this.webClient.get().uri("/repos/{username}/{name}", username, name)
            .retrieve().bodyToMono(Repository.class);
    }
}
