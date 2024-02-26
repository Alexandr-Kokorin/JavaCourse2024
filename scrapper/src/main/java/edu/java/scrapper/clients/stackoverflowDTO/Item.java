package edu.java.scrapper.clients.stackoverflowDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Item(
    @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
    @JsonProperty("last_edit_date") OffsetDateTime lastEditDate,
    @JsonProperty("creation_date") OffsetDateTime creationDate,
    @JsonProperty("answer_id") long answerId
) { }
