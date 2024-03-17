package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(ApplicationConfig.class)
public class GodOfLinks implements AutoCloseable, UpdatesListener {

    private final static Logger LOGGER = LogManager.getLogger();
    private TelegramBot bot;
    @Autowired
    private ApplicationConfig config;
    @Autowired
    private DialogManager dialogManager;

    @PostConstruct
    public void start() {
        bot = new TelegramBot(config.telegramToken());
        setListCommands();
        setUpdatesListener();
    }

    private void setListCommands() {
        SetMyCommands set = new SetMyCommands(
        new BotCommand("/start", "зарегистрировать пользователя"),
        new BotCommand("/track", "начать отслеживание ссылки"),
        new BotCommand("/untrack", "прекратить отслеживание ссылки"),
        new BotCommand("/list", "показать список отслеживаемых ссылок"),
        new BotCommand("/help", "вывести окно с командами"));
        bot.execute(set);
    }

    private void setUpdatesListener() {
        bot.setUpdatesListener(updates -> {
            process(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                LOGGER.error(e);
            }
        });
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Message message = update.message();
            if (message != null && message.text() != null) {
                sendMessage(message.chat().id(), dialogManager.sortMessage(message.chat().id(), message.text()));
            } else {
                LOGGER.error("Unsupported massage type!");
            }
        }
        return 0;
    }

    public void sendMessage(long id, List<String> list) {
        try {
            for (String text : list) {
                bot.execute(new SendMessage(id, text));
            }
        } catch (Exception e) {
            LOGGER.error("Чат " + id + " не найден!");
        }
    }

    @Override
    public void close() throws Exception {
        bot.shutdown();
    }
}
