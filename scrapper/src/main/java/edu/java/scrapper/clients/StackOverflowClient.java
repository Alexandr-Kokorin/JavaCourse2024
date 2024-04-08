package edu.java.scrapper.clients;

import edu.java.scrapper.clients.stackoverflowDTO.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {

    private final static Logger LOGGER = LogManager.getLogger();
    private final WebClient webClient;

    public StackOverflowClient() {
        webClient = WebClient.builder().baseUrl("https://api.stackexchange.com").build();
    }

    public StackOverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Retryable(maxAttemptsExpression = "#{@retry.maxAttempts}",
               retryFor = {RuntimeException.class, ResourceAccessException.class},
               backoff = @Backoff(
                   delayExpression = "#{@retry.delay}",
                   multiplierExpression = "#{@retry.multiplier}"
               ))
    public Question getInfo(long id) {
        return webClient.get().uri("/2.3/questions/{id}/answers?site=stackoverflow&filter=withbody", id)
            .retrieve().bodyToMono(Question.class).block();
    }

    @Recover
    private Question recoverGetInfo(long id) {
        LOGGER.error("Превышен лимит запросов к stackoverflow или отсутсвует интернет соединение!");
        return null;
    }
}
