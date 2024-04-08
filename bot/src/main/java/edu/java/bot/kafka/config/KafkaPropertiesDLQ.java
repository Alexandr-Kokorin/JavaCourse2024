package edu.java.bot.kafka.config;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka.producer", ignoreUnknownFields = false)
public record KafkaPropertiesDLQ(
    @NotNull String bootstrapServers,
    @NotNull String clientId,
    @NotNull String acksMode,
    @NotNull Duration deliveryTimeout,
    @NotNull Integer lingerMs,
    @NotNull Integer batchSize,
    @NotNull Integer maxInFlightPerConnection,
    @NotNull Boolean enableIdempotence
) { }
