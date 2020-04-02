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
    private String creator;

    @Column(nullable = false)
    private int players;

    private GameLength gameDuration;

    private boolean isPublic;

    @Transient
    private Thread gameThread;

    @Transient
    private List<Player> listOfPlayers;


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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getPlayers() {
        return players;
    }

    public void setGameDuration(GameLength gameDuration) { this.gameDuration = gameDuration; }

    public void setIsPublic(boolean isPublic) {this.isPublic = isPublic; }

    public Thread getGameThread() {return this.gameThread; }

    public void addPlayer(Player player) {
        listOfPlayers.add(player);
        this.players = listOfPlayers.size();
    }

    public void removePlayer(Player player) {
        listOfPlayers.remove(player);
        this.players = listOfPlayers.size();
    }

    public List<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public void startGame() { }

}
