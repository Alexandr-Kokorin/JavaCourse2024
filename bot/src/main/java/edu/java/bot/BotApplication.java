package edu.java.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotApplication {

    @Autowired
    private GodOfLinks bot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
