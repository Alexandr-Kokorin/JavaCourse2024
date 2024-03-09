package edu.java.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkRequest(
    @NotNull URI link
) { }
