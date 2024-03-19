package edu.java.scrapper.clients.githubDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Pull(
    @JsonProperty("number") int number,
    @JsonProperty("title") String title,
    @JsonProperty("created_at") OffsetDateTime createdAt
) { }
