package edu.java.scrapper.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.githubDTO.Commit;
import edu.java.scrapper.clients.githubDTO.GitHub;
import edu.java.scrapper.clients.githubDTO.Pull;
import edu.java.scrapper.domain.data.GitHubData;
import edu.java.scrapper.domain.dto.Link;
import io.swagger.v3.core.util.Json;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("MultipleStringLiterals")
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

    public GitHubData getGitHubData(Link link) {
        try {
            return Json.mapper().readValue(link.data(), GitHubData.class);
        } catch (JsonProcessingException e) {
            return new GitHubData(0, 0, 0, 0, 0);
        }
    }

    public List<String> getDescriptionNewCommit(GitHubData data, GitHub gitHub, Link link) {
        List<String> description = new ArrayList<>();
        if (data.numberOfCommits() != gitHub.commits().length) {
            for (Commit commit: gitHub.commits()) {
                if (commit.internalCommit().committer().createdTime().isAfter(link.lastUpdate())) {
                    description.add("Был добавлен новый коммит: " + commit.internalCommit().message());
                }
            }
        }
        return description;
    }

    public List<String> getDescriptionCreateOrDeletePull(GitHubData data, GitHub gitHub, Link link) {
        List<String> description = new ArrayList<>();
        if (data.numberOfPulls() != gitHub.pulls().length
            || data.pullsHash() != Arrays.toString(gitHub.pulls()).hashCode()) {
            int countNew = 0;
            for (Pull pull: gitHub.pulls()) {
                if (pull.createdAt().isAfter(link.lastUpdate())) {
                    description.add("Был добавлен новый Pull Request: " + pull.title());
                    countNew++;
                }
            }
            int countDeleted = data.numberOfPulls() - (gitHub.pulls().length - countNew);
            if (countDeleted > 0) {
                description.add("Было удалено " + countDeleted + " Pull Request.");
            }
        }
        return description;
    }

    public List<String> getDescriptionCreateOrDeleteBranch(GitHubData data, GitHub gitHub) {
        List<String> description = new ArrayList<>();
        if (data.numberOfBranches() != gitHub.branches().length
            || data.branchesHash() != Arrays.toString(gitHub.branches()).hashCode()) {
            int diff = gitHub.branches().length - data.numberOfBranches();
            if (diff > 0) {
                description.add("Было добавлено " + diff + " Branches.");
            } else if (diff < 0) {
                description.add("Было удалено " + (-diff) + " Branches.");
            } else {
                description.add("Произошли изменения в Branches.");
            }
        }
        return description;
    }
}
