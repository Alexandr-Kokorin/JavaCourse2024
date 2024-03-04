package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;

public record ApiErrorResponse(
    @NotEmpty String description,
    @NotEmpty String code,
    @NotEmpty String exceptionName,
    @NotEmpty String exceptionMessage,
    @NotEmpty String[] stacktrace
) { }
