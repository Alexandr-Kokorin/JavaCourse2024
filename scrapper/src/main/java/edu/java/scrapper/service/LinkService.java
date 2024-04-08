package edu.java.scrapper.service;

import edu.java.dto.ListLinksResponse;
import java.net.URI;

public interface LinkService {

    boolean add(long chatId, URI url);

    boolean remove(long chatId, URI url);

    ListLinksResponse listAll(long chatId);
}
