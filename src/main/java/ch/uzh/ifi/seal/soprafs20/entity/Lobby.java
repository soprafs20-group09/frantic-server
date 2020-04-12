package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.utils.FranticUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal Lobby Representation
 * This class composes the internal representation of the lobby and defines how the lobby is stored in the database.
 */
@Entity
@Table(name = "Lobby")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String lobbyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String creator;

    @Column(nullable = false)
    private int players;

    @ElementCollection
    private List<String> listOfPlayers;

    @Column
    private GameLength gameDuration;

    @Column
    private boolean isPublic;

    @Transient
    private boolean isPlaying;

    @Transient
    private Game game;


    public Lobby() {
        this.lobbyId = FranticUtils.generateId(8);
        this.gameDuration = GameLength.MEDIUM;
        this.isPublic = true;
        this.listOfPlayers = new ArrayList<>();
        this.isPlaying = false;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public GameLength getGameDuration() { return gameDuration; }

    public void setGameDuration(GameLength gameDuration) { this.gameDuration = gameDuration; }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {this.isPublic = isPublic; }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Game getGame() {return game; }


    public void addPlayer(Player player) {
        listOfPlayers.add(player.getUsername());
        this.players = listOfPlayers.size();
    }

    public void removePlayer(Player player) {
        listOfPlayers.remove(player.getUsername());
        this.players = listOfPlayers.size();
    }

    public List<String> getListOfPlayers() {
        return listOfPlayers;
    }

    public void startGame(GameService gameService) {
        //The game can only be started if there are more than one player in the lobby
        if (this.players < 2) {
            return;
        }
        this.game = new Game(lobbyId, gameDuration);
        this.game.startGame();
        this.isPlaying = true;
    }
}
