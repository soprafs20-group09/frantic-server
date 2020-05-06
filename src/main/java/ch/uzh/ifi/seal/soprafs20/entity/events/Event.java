package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;

import java.util.List;

public interface Event {
    String getName();
    void performEvent();
    String getMessage();
}