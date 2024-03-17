package edu.java.scrapper.service.handlers;

import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class LinkHandler {

    public String getType(URI url) {
        String type = "";
        if (url.toString().contains("github.com")) {
            type = "github";
        } else if (url.toString().contains("stackoverflow.com")) {
            type = "stackoverflow";
        }
        return type;
    }
}
