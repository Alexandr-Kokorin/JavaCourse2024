package edu.java.scrapper;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkUpdater;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static int COUNT_LINKS = 5;
    @Autowired
    private LinkUpdater linkUpdater;
    @Autowired
    private BotClient botClient;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        var links = linkUpdater.getLinks(COUNT_LINKS);
        for (Link link : links) {
            var linkUpdate = linkUpdater.update(link);
            if (Objects.nonNull(linkUpdate)) {
                botClient.sendUpdates(linkUpdate);
            }
        }
        LOGGER.info("updating...");
    }
}
