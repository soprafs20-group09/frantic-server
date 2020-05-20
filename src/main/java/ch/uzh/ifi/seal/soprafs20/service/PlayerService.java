package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles Player functionalities (create, register, remove)
 */
@Service
@Transactional
public class PlayerService {

    private static PlayerService instance;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
        instance = this;
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public synchronized Player createPlayer(String identity, String username) {
        Player newPlayer = new Player();
        newPlayer.setUsername(username);
        newPlayer.setIdentity(identity);
        this.playerRepository.save(newPlayer);
        this.playerRepository.flush();
        return newPlayer;
    }

    public synchronized RegisteredDTO registerPlayer(String identity, Player player, String lobbyId) {
        player.setIdentity(identity);
        this.playerRepository.flush();
        return new RegisteredDTO(player.getUsername(), lobbyId);
    }

    public synchronized String removePlayer(Player player) {
        Lobby currentLobby;
        try {
            currentLobby = this.lobbyRepository.findByLobbyId(player.getLobbyId());

            //remove player from lobby
            currentLobby.removePlayer(player);
            this.lobbyRepository.flush();

            //remove player from game
            if (currentLobby.isPlaying()) {
                GameRepository.findByLobbyId(currentLobby.getLobbyId()).playerLostConnection(player);
            }

            //remove player from PlayerRepository
            if (this.playerRepository.findByIdentity(player.getIdentity()) != null) {
                this.playerRepository.delete(player);
                this.playerRepository.flush();
            }
        }
        catch (NullPointerException e) {
            return null;
        }
        return currentLobby.getLobbyId();
    }

    public List<Player> getPlayersInLobby(String lobbyId) {
        List<Player> players = this.playerRepository.findByLobbyId(lobbyId);
        Lobby lobby = this.lobbyRepository.findByLobbyId(lobbyId);
        players.removeIf(player -> !lobby.getListOfPlayers().contains(player.getUsername()));
        return players;
    }
}
