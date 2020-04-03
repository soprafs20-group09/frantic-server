package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.PlayerServiceException;
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
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController extends WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private String base = "/queue/lobby/";

    public LobbyController(LobbyService lobbyService, PlayerService playerService, @Qualifier("playerRepository") PlayerRepository playerRepository,
                           @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        super(lobbyService, playerService, playerRepository, lobbyRepository);
    }

    @MessageMapping("/lobby/{lobbyId}/settings")
    public void changeLobbySettings(@DestinationVariable String lobbyId,
                                    SimpMessageHeaderAccessor sha, LobbySettingsDTO lobbySettingsDTO) throws Exception {
        String identity = sha.getUser().getName();
        if (checkSender(identity, lobbyId)) {
            LobbyStateDTO newLobbyState = lobbyService.updateLobbySettings(lobbyId, lobbySettingsDTO);

            sendToLobby(lobbyId, base,"/lobby-state", newLobbyState);
        }
    }

    @MessageMapping("/lobby/{lobbyId}/kick")
    public void kickPlayer(@DestinationVariable String lobbyId,
                           SimpMessageHeaderAccessor sha, KickDTO kickDTO) throws Exception {
        String identity = sha.getUser().getName();
        if (checkSender(identity, lobbyId)) {

            Player admin = playerRepository.findByIdentity(identity);
            if (!admin.isAdmin()) {
                throw new PlayerServiceException("Invalid action. Not admin.");
            }

            Player player = this.playerRepository.findByUsernameAndLobbyId(kickDTO.getUsername(), lobbyId);
            DisconnectDTO disconnectDTO = this.lobbyService.kickPlayer();
            simpMessagingTemplate.convertAndSendToUser(player.getIdentity(),
                    "/queue/disconnect", disconnectDTO);

            // send new lobby state to remaining players
            sendChatPlayerNotification(lobbyId, player.getUsername() + " was kicked!", player.getUsername());
        }
    }

    @MessageMapping("/lobby/{lobbyId}/chat")
    public void newChatMessage(@DestinationVariable String lobbyId,
                               SimpMessageHeaderAccessor sha, ChatDTO chatDTO) throws Exception {
        String identity = sha.getUser().getName();
        if (checkSender(identity, lobbyId)) {
            Player sender = this.playerRepository.findByIdentity(identity);

            chatDTO.setType("msg");
            chatDTO.setUsername(sender.getUsername());

            sendToLobby(lobbyId, base,"/chat", chatDTO);
        }
    }


}
