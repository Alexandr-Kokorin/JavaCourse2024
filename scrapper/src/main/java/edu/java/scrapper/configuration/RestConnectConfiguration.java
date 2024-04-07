package edu.java.scrapper.configuration;

import edu.java.scrapper.BotClient;
import edu.java.scrapper.connectBot.RestConnect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "connect-type", havingValue = "rest")
public class RestConnectConfiguration {

    @Bean
    public RestConnect restConnect(BotClient botClient) {
        return new RestConnect(botClient);
    }
}
