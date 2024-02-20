package edu.java.clients;

import edu.java.clients.stackoverflowDTO.Question;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient() {
        this.webClient = WebClient.builder().baseUrl("https://api.stackexchange.com").build();
    }

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<Question> getQuestionInfo(long id) {
        return this.webClient.get().uri("/2.3/questions/{id}/answers?site=stackoverflow&filter=withbody", id)
            .retrieve().bodyToMono(Question.class);
    }
}
