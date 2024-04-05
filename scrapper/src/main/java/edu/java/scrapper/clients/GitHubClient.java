package edu.java.scrapper.clients;

import edu.java.scrapper.clients.githubDTO.Branch;
import edu.java.scrapper.clients.githubDTO.Commit;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.clients.githubDTO.Pull;
import edu.java.scrapper.clients.githubDTO.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    private final static Logger LOGGER = LogManager.getLogger();
    private final WebClient webClient;

    public GitHubClient() {
        webClient = WebClient.builder().baseUrl("https://api.github.com").build();
    }

    public GitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public GitHub getInfo(String username, String name) {
        var repository = webClient.get().uri("/repos/{username}/{name}", username, name)
            .retrieve().bodyToMono(Repository.class).block();
        var commits = webClient.get().uri("/repos/{username}/{name}/commits", username, name)
            .retrieve().bodyToMono(Commit[].class).block();
        var pulls = webClient.get().uri("/repos/{username}/{name}/pulls", username, name)
            .retrieve().bodyToMono(Pull[].class).block();
        var branches = webClient.get().uri("/repos/{username}/{name}/branches", username, name)
            .retrieve().bodyToMono(Branch[].class).block();
        return new GitHub(repository, commits, pulls, branches);
    }

    @Recover
    private GitHub recoverGetInfo(String username, String name) {
        LOGGER.error("Превышен лимит запросов к github или отсутсвует интернет соединение!");
        return null;
    }
}
