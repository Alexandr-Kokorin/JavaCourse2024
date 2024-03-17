package edu.java.scrapper.domain.dto;

import io.swagger.v3.core.util.Json;
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
