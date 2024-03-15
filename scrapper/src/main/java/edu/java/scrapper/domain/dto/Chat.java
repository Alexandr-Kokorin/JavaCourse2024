package edu.java.scrapper.domain.dto;

import java.time.OffsetDateTime;

public record Chat(
    long id,
    String name,
    String state,
    OffsetDateTime createdAt
) { }
