package edu.java.bot.kafka;

import edu.java.bot.service.UpdateService;
import edu.java.dto.LinkUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UpdateListener {

    @Autowired
    private UpdateService updateService;
    @Autowired
    private BotQueueProducerDLQ botQueueProducerDLQ;

    @KafkaListener(topics = "scrapper.notifications", containerFactory = "botContainerFactory")
    public void handleMessage(@Payload LinkUpdate linkUpdate, Acknowledgment acknowledgment) {
        try {
            updateService.sendUpdates(linkUpdate);
        } catch (Exception e) {
            botQueueProducerDLQ.send(linkUpdate);
        }
        acknowledgment.acknowledge();
    }
}
