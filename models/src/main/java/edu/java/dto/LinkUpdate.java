package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkUpdate(
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty long[] tgChatIds
) { }
