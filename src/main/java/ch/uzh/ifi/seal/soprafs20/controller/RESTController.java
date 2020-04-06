package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyListElementDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class RESTController {

    private static Map<String, String[]> authMap = new HashMap<>();

    private final LobbyService lobbyService;

    RESTController(LobbyService lobbyService, PlayerService playerService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyListElementDTO> getAllLobbies(@RequestParam(required = false) String q) {

        List<LobbyListElementDTO> ret = new ArrayList<>();
        List<Lobby> l = lobbyService.getLobbies(q);
        for (Lobby lobby : l) {
            ret.add(DTOMapper.INSTANCE.convertLobbyToLobbyListDTO(lobby));
        }
        return ret;
    }

    @GetMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerScoreDTO> getAllPlayerScores(@PathVariable String id) {

        return lobbyService.getScores(id);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO createLobby(@RequestBody PlayerUsernameDTO playerUsernameDTO) {

        lobbyService.checkLobbyCreate(playerUsernameDTO.getUsername());

        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, new String[]{playerUsernameDTO.getUsername()});

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(authToken);
        response.setName(playerUsernameDTO.getUsername() + "'s lobby");
        response.setUsername(playerUsernameDTO.getUsername());
        return response;
    }

    @PutMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO joinLobby(@PathVariable String id, @RequestBody PlayerUsernameDTO playerUsernameDTO) {

        lobbyService.checkLobbyJoin(id, playerUsernameDTO.getUsername());

        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, new String[]{playerUsernameDTO.getUsername(), String.valueOf(id)});

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(authToken);
        response.setName(playerUsernameDTO.getUsername() + "'s lobby");
        response.setUsername(playerUsernameDTO.getUsername());
        return response;
    }

    public static String getUsernameFromAuthToken(String authToken) {
        return authMap.get(authToken)[0];
    }

    public static String getLobbyIdFromAuthToken(String authToken) {
        if (authMap.get(authToken).length > 1) {
            return authMap.get(authToken)[1];
        }
        return null;
    }

    public static void removeFromAuthMap(String authToken) {
        authMap.remove(authToken);
    }
}
