package edu.java.scrapper.configuration;

import edu.java.scrapper.connectBot.KafkaConnect;
import edu.java.scrapper.kafka.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "connect-type", havingValue = "kafka")
public class KafkaConnectConfiguration {

    @Bean
    public KafkaConnect kafkaConnect(ScrapperQueueProducer scrapperQueueProducer) {
        return new KafkaConnect(scrapperQueueProducer);
    }
}
