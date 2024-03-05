package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;

public record LinkUpdate(
    @Positive long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty long[] tgChatIds
) { }
