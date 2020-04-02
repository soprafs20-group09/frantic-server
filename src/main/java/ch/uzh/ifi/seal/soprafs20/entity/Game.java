package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

import java.util.List;

public class Game implements Runnable {

    private GameLength gameDuration;

    private List listOfPlayers;

    public Game(GameLength gameDuration, List listOfPlayers) {
        this.gameDuration = gameDuration;
        this.listOfPlayers = listOfPlayers;
    }

    @Override
    public void run() {
    }
}
