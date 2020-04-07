package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "Lobby")
public class Lobby implements Serializable {

    private Random random = new Random();

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
    private List<Player> playerRepresentations;

    @Transient
    private boolean isPlaying;

    @Transient
    private Game game;

    @Transient
    private Thread gameThread;


    public Lobby() {
        this.lobbyId = generateLobbyId();
        this.gameDuration = GameLength.MEDIUM;
        this.isPublic = true;
        this.listOfPlayers = new ArrayList<>();
        this.playerRepresentations = new ArrayList<>();
        this.isPlaying = false;
    }

    private String generateLobbyId() {
        StringBuilder s = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(chars.length());
            s.append(chars.charAt(idx));
        }
        return s.toString();
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

    public Thread getGameThread() {return this.gameThread; }

    public void addPlayer(Player player) {
        listOfPlayers.add(player.getUsername());
        playerRepresentations.add(player);
        this.players = listOfPlayers.size();
    }

    public void removePlayer(Player player) {
        listOfPlayers.remove(player.getUsername());
        playerRepresentations.remove(player);
        this.players = listOfPlayers.size();
    }

    public List<String> getListOfPlayers() {
        return listOfPlayers;
    }

    public void startGame() {
        //The game can only be started if there are more than one player in the lobby
        if (this.players < 2) {
            return;
        }
        this.game = new Game(gameDuration, playerRepresentations);
        this.gameThread = new Thread(game);
        gameThread.start();
        this.isPlaying = true;
    }
}
