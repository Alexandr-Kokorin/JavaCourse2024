package edu.java.scrapper.service.handlers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> getDescriptionUnknownUpdate(List<String> desc) {
        List<String> description = new ArrayList<>();
        if (desc.isEmpty()) {
            description.add("Произошло неизвестное обновление ресурса.");
        }
        return description;
    }
}
