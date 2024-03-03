package edu.java.bot.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.net.URI;

public record LinkUpdate(
    @Positive long id,
    @NotEmpty URI url,
    @NotEmpty String description,
    @NotEmpty long[] tgChatIds
) { }
