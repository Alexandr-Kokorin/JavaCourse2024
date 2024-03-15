package edu.java.scrapper.service;

import edu.java.dto.StateResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    boolean add(long chatId, String name);

    boolean remove(long chatId);

    StateResponse getState(long chatId);

    void setState(long chatId, String state);
}
