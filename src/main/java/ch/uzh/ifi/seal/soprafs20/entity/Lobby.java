package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameLength;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Player host;

    @Column(nullable = false)
    private int size;

    private GameLength gameDuration;

    private boolean isPublic;

    private Thread gameThread;

    private List players;


    public Lobby() {
        this.gameDuration = GameLength.MEDIUM;
        this.isPublic = false;
    }


    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public int getSize() {
        return size;
    }

    public void setGameDuration(GameLength gameDuration) { this.gameDuration = gameDuration; }

    public void setIsPublic(boolean isPublic) {this.isPublic = isPublic; }

    public Thread getGameThread() {return this.gameThread; }

    public void addPlayer(Player player) {
        players.add(player);
        this.size = players.size();
    }

    public void removePlayer(Player player) {
        players.remove(player);
        this.size = players.size();
    }

    public void startGame() { }

}
