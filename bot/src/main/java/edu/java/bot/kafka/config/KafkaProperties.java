package edu.java.bot.kafka.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka.consumer", ignoreUnknownFields = false)
public record KafkaProperties(
    @NotNull String bootstrapServers,
    @NotNull String groupId,
    @NotNull String autoOffsetReset,
    @NotNull Integer maxPollIntervalMs,
    @NotNull Boolean enableAutoCommit,
    @NotNull Integer concurrency,
    @NotNull String schemaRegistryUrl
) { }
