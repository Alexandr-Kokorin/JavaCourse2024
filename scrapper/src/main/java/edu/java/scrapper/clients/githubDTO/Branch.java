package edu.java.scrapper.clients.githubDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Branch(
    @JsonProperty("name") String name
) { }
