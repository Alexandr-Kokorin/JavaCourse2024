package edu.java.scrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.LinkUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

public class BotClient {

    private final static Logger LOGGER = LogManager.getLogger();
    private final RestClient restClient;

    public BotClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8090").build();
    }

    public BotClient(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) {
        try {
            return restClient.post().uri("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ObjectMapper().writeValueAsString(linkUpdate))
                .exchange((request, response) -> new ResponseEntity<>(response.getStatusCode()));
        } catch (JsonProcessingException e) {
            LOGGER.error("Ошибка создания JSON!");
            throw new RuntimeException(e);
        }
    }

    @Recover
    private ResponseEntity<Void> recoverSendUpdates(LinkUpdate linkUpdate) {
        LOGGER.error("Бот недоступен!");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
