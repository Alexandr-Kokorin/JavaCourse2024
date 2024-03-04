package edu.java.bot;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Void> addTgChat(long id) {
        return restClient.post().uri("/tg-chat/{id}", id).retrieve().toBodilessEntity();
    }

    public ResponseEntity<Void> deleteTgChat(long id) {
        return restClient.delete().uri("/tg-chat/{id}", id).retrieve().toBodilessEntity();
    }

    public ResponseEntity<ListLinksResponse> getLinks(long id) {
        return restClient.get().uri("/links").header("Tg-Chat-Id", String.valueOf(id))
            .retrieve().toEntity(ListLinksResponse.class);
    }

    public ResponseEntity<LinkResponse> addLink(long id, URI link) {
        return restClient.post().uri("/links").header("Tg-Chat-Id", String.valueOf(id))
            .body(new AddLinkRequest(link)).retrieve().toEntity(LinkResponse.class);
    }

    public ResponseEntity<LinkResponse> deleteLink(long id, URI link) {
        return restClient.post().uri("/links/delete").header("Tg-Chat-Id", String.valueOf(id))
            .body(new RemoveLinkRequest(link)).retrieve().toEntity(LinkResponse.class);
    }
}
