package edu.java.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.states.DialogState;
import edu.java.dto.LinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.StateResponse;
import java.net.URI;
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

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {

    private final static Logger LOGGER = LogManager.getLogger();
    private final RestClient restClient;

    public ScrapperClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public ScrapperClient(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> addTgChat(long id, String name) {
        return restClient.post().uri("/tg-chat/{id}", id)
            .header("Name", name)
            .exchange((request, response) -> new ResponseEntity<>(response.getStatusCode()));
    }

    @Recover
    private ResponseEntity<Void> recoverAddTgChat(long id, String name) {
        printLog();
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> deleteTgChat(long id) {
        return restClient.delete().uri("/tg-chat/{id}", id)
            .exchange((request, response) -> new ResponseEntity<>(response.getStatusCode()));
    }

    @Recover
    private ResponseEntity<Void> recoverDeleteTgChat(long id) {
        printLog();
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<StateResponse> getState(long id) {
        return restClient.get().uri("/state/{id}", id).retrieve().toEntity(StateResponse.class);
    }

    @Recover
    private ResponseEntity<StateResponse> recoverGetState(long id) {
        printLog();
        return new ResponseEntity<>(new StateResponse("NONE"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> updateState(long id, DialogState state) {
        return restClient.post().uri("/state/{id}", id).header("State", state.toString())
            .retrieve().toBodilessEntity();
    }

    @Recover
    private ResponseEntity<Void> recoverUpdateState(long id, DialogState state) {
        printLog();
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<ListLinksResponse> getLinks(long id) {
        return restClient.get().uri("/links").header("Tg-Chat-Id", String.valueOf(id))
            .retrieve().toEntity(ListLinksResponse.class);
    }

    @Recover
    private ResponseEntity<ListLinksResponse> recoverGetLinks(long id) {
        printLog();
        return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> addLink(long id, URI link) {
        try {
            return restClient.post().uri("/links/add").header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ObjectMapper().writeValueAsString(new LinkRequest(link)))
                .exchange((request, response) -> new ResponseEntity<>(response.getStatusCode()));
        } catch (JsonProcessingException e) {
            LOGGER.error("Ошибка создания JSON!");
            throw new RuntimeException(e);
        }
    }

    @Recover
    private ResponseEntity<Void> recoverLink(long id, URI link) {
        printLog();
        return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public ResponseEntity<Void> deleteLink(long id, URI link) {
        try {
            return restClient.post().uri("/links/delete").header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ObjectMapper().writeValueAsString(new LinkRequest(link)))
                .exchange((request, response) -> new ResponseEntity<>(response.getStatusCode()));
        } catch (JsonProcessingException e) {
            LOGGER.error("Ошибка создания JSON!");
            throw new RuntimeException(e);
        }
    }

    private void printLog() {
        LOGGER.error("Сервер недоступен!");
    }
}
