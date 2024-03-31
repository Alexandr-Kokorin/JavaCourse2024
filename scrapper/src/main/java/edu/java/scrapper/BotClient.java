package edu.java.scrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClient;

public class BotClient {

    private final RestClient restClient;

    public BotClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8090").build();
    }

    public BotClient(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "${retry-max-attempts}",
               backoff = @Backoff(delayExpression = "${retry-delay}"))
    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) throws JsonProcessingException {
        return restClient.post().uri("/links")
            .header("Content-Type", "application/json;charset=UTF-8")
            .body(new ObjectMapper().writeValueAsString(linkUpdate))
            .retrieve().toBodilessEntity();
    }
}
