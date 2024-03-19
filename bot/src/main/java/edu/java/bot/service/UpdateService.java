package edu.java.bot.service;

import edu.java.bot.GodOfLinks;
import edu.java.dto.LinkUpdate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {

    @Autowired
    private GodOfLinks bot;

    public void sendUpdates(LinkUpdate linkUpdate) {
        StringBuilder description = new StringBuilder();
        for (String desc : linkUpdate.description()) {
            description.append(desc).append("\n");
        }
        for (long chatId : linkUpdate.tgChatIds()) {
            bot.sendMessage(
                chatId,
                List.of("По ссылке " + linkUpdate.url() + " произошло обновление." + "\n\n"
                    + "Описание:" + "\n" + description)
            );
        }
    }
}
