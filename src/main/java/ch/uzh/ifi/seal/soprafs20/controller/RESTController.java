package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyListElementDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class RESTController {

    private final Logger log = LoggerFactory.getLogger(RESTController.class);

    private static Map<String, String[]> authMap = new HashMap<>();

    private final LobbyService lobbyService;

    RESTController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyListElementDTO> getAllLobbies(@RequestParam(required = false) String q) {

        log.debug(q == null ? "GET /lobbies" : "GET /lobbies?q=" + q);

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

        log.debug("GET /lobbies/" + id);

        return lobbyService.getScores(id);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO createLobby(@Valid @RequestBody PlayerUsernameDTO playerUsernameDTO) {

        String username = clean(playerUsernameDTO.getUsername());

        log.debug("POST /lobbies, body: " + playerUsernameDTO.toString());
        lobbyService.checkLobbyCreate(username);

        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, new String[]{username});

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(authToken);
        response.setName(username + "'s lobby");
        response.setUsername(username);
        return response;
    }

    @PutMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO joinLobby(@PathVariable String id, @RequestBody PlayerUsernameDTO playerUsernameDTO) {

        String username = clean(playerUsernameDTO.getUsername());

        log.debug("PUT /lobbies/" + id + ", body: " + playerUsernameDTO.toString());
        lobbyService.checkLobbyJoin(id, username);

        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, new String[]{username, String.valueOf(id)});

        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(authToken);
        response.setName(username + "'s lobby");
        response.setUsername(username);
        return response;
    }

    public static String getUsernameFromAuthToken(String authToken) {
        if (authMap.size() > 0) {
            return authMap.get(authToken)[0];
        }
        else {
            return null;
        }
    }

    public static String getLobbyIdFromAuthToken(String authToken) {
        if (authMap.containsKey(authToken)) {
            if (authMap.get(authToken).length > 1) {
                return authMap.get(authToken)[1];
            }
            return null;
        }
        return null;
    }

    public static void removeFromAuthMap(String authToken) {
        authMap.remove(authToken);
    }

    private String clean(String arg) {
        return Jsoup.clean(arg, Whitelist.basic());
    }
}
