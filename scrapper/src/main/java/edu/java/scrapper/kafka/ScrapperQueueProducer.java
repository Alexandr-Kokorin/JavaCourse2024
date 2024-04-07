package edu.java.scrapper.kafka;

import edu.java.dto.LinkUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScrapperQueueProducer {

    private final static Logger LOGGER = LogManager.getLogger();
    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    public void send(LinkUpdate update) {
        try {
            kafkaTemplate.send("scrapper.notifications", update);
        } catch (Exception e) {
            LOGGER.error("Error occurred during sending to Kafka", e);
        }
    }
}
