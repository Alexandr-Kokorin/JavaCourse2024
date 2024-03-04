package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;

public record AddLinkRequest(
    @NotEmpty URI link
) { }
