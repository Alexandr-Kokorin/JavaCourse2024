package edu.java.scrapper.clients;

import edu.java.scrapper.clients.stackoverflowDTO.Question;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient() {
        webClient = WebClient.builder().baseUrl("https://api.stackexchange.com").build();
    }

    public StackOverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "${retry-max-attempts}",
               backoff = @Backoff(delayExpression = "${retry-delay}"))
    public Question getInfo(long id) {
        return webClient.get().uri("/2.3/questions/{id}/answers?site=stackoverflow&filter=withbody", id)
            .retrieve().bodyToMono(Question.class).block();
    }
}
