package edu.java.scrapper.clients.githubDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Commit(
    @JsonProperty("commit") InternalCommit internalCommit
) {
    public record  InternalCommit (
        @JsonProperty("committer") Committer committer,
        @JsonProperty("message") String message
    ) {
        public record Committer (
            @JsonProperty("date") OffsetDateTime createdTime
        ) { }
    }
}
