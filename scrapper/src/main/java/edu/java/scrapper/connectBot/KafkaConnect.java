package edu.java.scrapper.connectBot;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.kafka.ScrapperQueueProducer;

public class KafkaConnect implements Connect {

    private final ScrapperQueueProducer scrapperQueueProducer;

    public KafkaConnect(ScrapperQueueProducer scrapperQueueProducer) {
        this.scrapperQueueProducer = scrapperQueueProducer;
    }

    @Override
    public void send(LinkUpdate linkUpdate) {
        scrapperQueueProducer.send(linkUpdate);
    }
}
