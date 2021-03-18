package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameRepository {

    private static final HashMap<String, Game> gameRepo = new HashMap<>();

    public static void addGame(String lobbyId, Game game) {
        gameRepo.put(lobbyId, game);
    }

    public static void removeGame(String lobbyId) {
        if (gameRepo.containsKey(lobbyId)) {
            gameRepo.get(lobbyId).stopTimer();
            gameRepo.remove(lobbyId);
        }
    }

    public static Game findByLobbyId(String lobbyId) {
        return gameRepo.get(lobbyId);
    }
}
