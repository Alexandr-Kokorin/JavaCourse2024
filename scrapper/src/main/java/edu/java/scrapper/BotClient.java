package edu.java.scrapper;

import edu.java.scrapper.api.dto.LinkUpdate;
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

    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) {
        return restClient.post().uri("/links").body(linkUpdate).retrieve().toBodilessEntity();
    }
}
