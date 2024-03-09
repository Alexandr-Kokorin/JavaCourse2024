package edu.java.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;

public record LinkResponse(
    @Positive long id,
    @NotNull URI url
) { }
