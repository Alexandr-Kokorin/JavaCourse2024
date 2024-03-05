package edu.java.dto;

import jakarta.validation.constraints.NotEmpty;

public record StateResponse(
    @NotEmpty String state
) { }
