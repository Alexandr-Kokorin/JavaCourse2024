package edu.java.scrapper.clients.stackoverflowDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record Question(
    List<Item> items
) {
    public record Item(
        @JsonProperty("owner") Owner owner,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
        @JsonProperty("creation_date") OffsetDateTime creationDate
    ) {
        public record Owner(

            @JsonProperty("display_name") String name
        ) { }
    }
}
