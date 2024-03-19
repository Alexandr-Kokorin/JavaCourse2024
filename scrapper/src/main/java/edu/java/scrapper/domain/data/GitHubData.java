package edu.java.scrapper.domain.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubData(
    @JsonProperty int numberOfCommits,
    @JsonProperty int numberOfBranches,
    @JsonProperty int numberOfPulls,
    @JsonProperty int branchesHash,
    @JsonProperty int pullsHash
) { }
