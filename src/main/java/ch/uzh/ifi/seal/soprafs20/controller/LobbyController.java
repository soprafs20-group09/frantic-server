package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LobbyController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    public LobbyController(@Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    @MessageMapping("/lobby/{lobbyId}/settings")
    public void changeLobbySettings(@DestinationVariable String lobbyId, LobbySettingsDTO lobbySettingsDTO) throws Exception {
        // do some operations
        simpMessagingTemplate.convertAndSend("/topic/lobby/" + lobbyId + "/lobby-state", new LobbyStateDTO());
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void changeLobbySettings(@DestinationVariable Long lobbyId,
                                    SimpMessageHeaderAccessor sha, ChatDTO chatDTO) throws Exception {
        String token = sha.getUser().getName();
        if(checkSender(token, lobbyId)) {
            Player sender = this.playerRepository.findByToken(token);

            chatDTO.setType("msg");
            chatDTO.setUsername(sender.getUsername());

            List<Player> allPlayersInLobby = this.playerRepository.findByLobbyId(lobbyId);
            for (Player player : allPlayersInLobby) {
                simpMessagingTemplate.convertAndSendToUser(player.getToken(),
                        "/topic/lobby/" + lobbyId + "/chat", chatDTO);
            }
        }
    }

    private boolean checkSender(String token, long lobbyId) {

        Player toCheck = this.playerRepository.findByToken(token);
        return toCheck.getLobbyId().equals(lobbyId);
    }
}
