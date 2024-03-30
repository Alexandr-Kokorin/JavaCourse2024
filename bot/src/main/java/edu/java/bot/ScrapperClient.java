package edu.java.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.states.DialogState;
import edu.java.dto.LinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.StateResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {

    private final RestClient restClient;

    public ScrapperClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public ScrapperClient(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public ResponseEntity<Void> addTgChat(long id, String name) {
        try {
            return restClient.post().uri("/tg-chat/{id}", id).header("Name", name).retrieve().toBodilessEntity();
        } catch (HttpServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<Void> deleteTgChat(long id) {
        try {
            return restClient.delete().uri("/tg-chat/{id}", id).retrieve().toBodilessEntity();
        } catch (HttpServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<StateResponse> getState(long id) {
        return restClient.get().uri("/state/{id}", id).retrieve().toEntity(StateResponse.class);
    }

    public ResponseEntity<Void> updateState(long id, DialogState state) {
        return restClient.post().uri("/state/{id}", id).header("State", state.toString())
            .retrieve().toBodilessEntity();
    }

    public ResponseEntity<ListLinksResponse> getLinks(long id) {
        return restClient.get().uri("/links").header("Tg-Chat-Id", String.valueOf(id))
            .retrieve().toEntity(ListLinksResponse.class);
    }

    public ResponseEntity<Void> addLink(long id, URI link) throws JsonProcessingException {
        try {
            return restClient.post().uri("/links/add").header("Tg-Chat-Id", String.valueOf(id))
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(new ObjectMapper().writeValueAsString(new LinkRequest(link))).retrieve().toBodilessEntity();
        } catch (HttpServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<Void> deleteLink(long id, URI link) throws JsonProcessingException {
        try {
            return restClient.post().uri("/links/delete").header("Tg-Chat-Id", String.valueOf(id))
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(new ObjectMapper().writeValueAsString(new LinkRequest(link))).retrieve().toBodilessEntity();
        } catch (HttpServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
