package edu.java.clients.githubDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(
    @JsonProperty("name") String name,
    @JsonProperty("created_at") OffsetDateTime createdTime,
    @JsonProperty("updated_at") OffsetDateTime updatedTime,
    @JsonProperty("pushed_at") OffsetDateTime pushedTime
) { }
