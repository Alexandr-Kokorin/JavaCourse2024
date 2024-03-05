package edu.java.scrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class BotClient {

    private final RestClient restClient;

    public BotClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8090").build();
    }

    public BotClient(String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) throws JsonProcessingException {
        return restClient.post().uri("/links").body(new ObjectMapper().writeValueAsString(linkUpdate))
            .retrieve().toBodilessEntity();
    }
}
