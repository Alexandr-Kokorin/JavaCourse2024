package edu.java.scrapper.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    long id,
    URI url,
    String type,
    String data,
    OffsetDateTime lastUpdate,
    OffsetDateTime lastCheck
) { }
