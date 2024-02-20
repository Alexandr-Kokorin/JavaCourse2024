package edu.java;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    private final static Logger LOGGER = LogManager.getLogger();

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        LOGGER.info("updating...");
    }
}
