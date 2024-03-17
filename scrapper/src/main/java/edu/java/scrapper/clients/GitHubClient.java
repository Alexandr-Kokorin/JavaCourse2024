package edu.java.scrapper.clients;

import edu.java.scrapper.clients.githubDTO.Branch;
import edu.java.scrapper.clients.githubDTO.Commit;
import edu.java.scrapper.clients.githubDTO.Pull;
import edu.java.scrapper.clients.githubDTO.Repository;
import edu.java.scrapper.clients.githubDTO.GitHub;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient() {
        webClient = WebClient.builder().baseUrl("https://api.github.com").build();
    }

    public GitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

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
}
