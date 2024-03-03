package edu.java.bot.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record ApiErrorResponse(
    @NotEmpty String type,
    @NotEmpty String exceptionName,
    @NotEmpty String exceptionMessage,
    @NotEmpty String[] stacktrace
) { }
