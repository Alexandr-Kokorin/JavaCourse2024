package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.net.URI;

public record LinkResponse(
    @Positive long id,
    @NotEmpty URI url
) { }
