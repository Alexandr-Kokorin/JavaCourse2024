package edu.java.scrapper.domain.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowData(
    @JsonProperty int numberOfAnswers
) { }
