package edu.java.scrapper.clients.githubDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(
    @JsonProperty("pushed_at") OffsetDateTime pushedTime
) { }
