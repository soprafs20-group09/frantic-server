package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class WebSocketController {

    @Autowired
    protected SimpMessagingTemplate simp;

    protected final LobbyService lobbyService;
    protected final PlayerService playerService;

    protected final PlayerRepository playerRepository;
    protected final LobbyRepository lobbyRepository;

    public WebSocketController(LobbyService lobbyService, PlayerService playerService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    protected boolean checkSender(String identity, long lobbyId) {
        Player toCheck = this.playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    protected void sendToLobby(long lobbyId, String base, String destination, Object dto) {
        List<Player> lobby = this.playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            simp.convertAndSendToUser(player.getIdentity(),
                    base + lobbyId + destination, dto);
        }
    }
}
