package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record ListLinksResponse(
    @NotEmpty LinkResponse[] links,
    @Positive int size
) { }
