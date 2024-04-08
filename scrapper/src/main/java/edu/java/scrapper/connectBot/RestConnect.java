package edu.java.scrapper.connectBot;

import edu.java.dto.LinkUpdate;
import edu.java.scrapper.BotClient;

public class RestConnect implements Connect {

    private final BotClient botClient;

    public RestConnect(BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public void send(LinkUpdate linkUpdate) {
        botClient.sendUpdates(linkUpdate);
    }
}
