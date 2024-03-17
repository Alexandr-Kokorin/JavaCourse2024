package edu.java.scrapper.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.domain.data.GitHubData;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubHandler {

    @Autowired
    private GitHubClient gitHubClient;

    public GitHub getInfo(URI url) {
        var parts = url.toString().split("/");
        return gitHubClient.getInfo(parts[parts.length - 2], parts[parts.length - 1]);
    }

    public String getData(GitHub gitHub) {
        try {
            return Json.mapper().writeValueAsString(new GitHubData(
                gitHub.commits().length,
                gitHub.branches().length,
                gitHub.pulls().length,
                Arrays.toString(gitHub.pulls()).hashCode(),
                Arrays.toString(gitHub.branches()).hashCode()
            ));
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
