package edu.java.bot.configuration;

import edu.java.bot.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient();
    }
}
