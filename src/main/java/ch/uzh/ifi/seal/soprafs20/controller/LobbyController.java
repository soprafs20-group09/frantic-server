package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.repository.LobbyRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.ChatDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.KickDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.DisconnectDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LobbyController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final LobbyService lobbyService;
    private final PlayerService playerService;

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;

    private String base = "/topic/lobby";

    public LobbyController(LobbyService lobbyService, PlayerService playerService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.lobbyRepository = lobbyRepository;
    }

    @MessageMapping("/lobby/{lobbyId}/settings")
    public void changeLobbySettings(@DestinationVariable long lobbyId,
                                    SimpMessageHeaderAccessor sha, LobbySettingsDTO lobbySettingsDTO) throws Exception {
        String token = sha.getUser().getName();
        if (checkSender(token, lobbyId)) {
            Lobby lobbyToUpdate = this.lobbyRepository.findByLobbyId(lobbyId);
            LobbyStateDTO newLobbyState = lobbyService.updateLobbySettings(lobbyToUpdate, lobbySettingsDTO);

            //TODO: add player array to DTO

            sendToLobby(lobbyId, "/lobby-state", newLobbyState);
        }
    }

    @MessageMapping("/lobby/{lobbyId}/kick")
    public void kickPlayer(@DestinationVariable long lobbyId,
                           SimpMessageHeaderAccessor sha, KickDTO kickDTO) throws Exception {
        String token = sha.getUser().getName();
        if (checkSender(token, lobbyId)) {

            // TODO: has to be admin

            Player player = this.playerRepository.findByUsernameAndLobbyId(kickDTO.getUsername(), lobbyId);
            DisconnectDTO disconnectDTO = this.lobbyService.kickPlayer(player);
            simpMessagingTemplate.convertAndSendToUser(player.getIdentity(),
                    base + lobbyId + "/disconnect", disconnectDTO);

            StompHeaderAccessor disconnect = StompHeaderAccessor.create(StompCommand.DISCONNECT);
            simpMessagingTemplate.convertAndSendToUser(player.getIdentity(), "/", disconnect);

            // send new lobby state to remaining players
            LobbyStateDTO newLobbyState = this.lobbyService.getLobbyState(lobbyId);
            sendToLobby(lobbyId, "/lobby-state", newLobbyState);
        }
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable long lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO chatDTO) throws Exception {
        String identity = sha.getUser().getName();
        if (checkSender(identity, lobbyId)) {
            Player sender = this.playerRepository.findByIdentity(identity);

            chatDTO.setType("msg");
            chatDTO.setUsername(sender.getUsername());

            sendToLobby(lobbyId, "/chat", chatDTO);
        }
    }

    private boolean checkSender(String identity, long lobbyId) {
        Player toCheck = this.playerRepository.findByIdentity(identity);
        return toCheck.getLobbyId().equals(lobbyId);
    }

    private void sendToLobby(long lobbyId, String destination, Object dto) {
        List<Player> lobby = this.playerRepository.findByLobbyId(lobbyId);
        for (Player player : lobby) {
            simpMessagingTemplate.convertAndSendToUser(player.getIdentity(),
                    base + lobbyId + destination, dto);
        }
    }
}
