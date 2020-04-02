package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyListElementDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class RESTController {

    private final LobbyService lobbyService;
    private final PlayerService playerService;

    RESTController(LobbyService lobbyService, PlayerService playerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyListElementDTO> getAllLobbies(@RequestParam(required = false) String q) {

        // dummy response
        if (q != null && q.equals("test")) {
            LobbyListElementDTO lobby = new LobbyListElementDTO();
            lobby.setLobbyId(1L);
            lobby.setName("foo's lobby");
            lobby.setCreator("foo");
            lobby.setPlayers(1);
            return Collections.singletonList(lobby);
        }
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
    public List<PlayerScoreDTO> getAllPlayerScores(@PathVariable long id) {

        // dummy response
        if (id == 200L) {
            PlayerScoreDTO score = new PlayerScoreDTO();
            score.setUsername("foo");
            score.setScore(69);
            return Collections.singletonList(score);
        } else if (id == 404L) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return lobbyService.getScores(id);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO createLobby(@RequestBody PlayerUsernameDTO playerUsernameDTO) {

        // dummy response
        if (playerUsernameDTO.getUsername().equals("test")) {
            LobbyJoinDTO join = new LobbyJoinDTO();
            join.setToken(UUID.randomUUID().toString());
            join.setName(playerUsernameDTO.getUsername() + "'s lobby");
            join.setUsername(playerUsernameDTO.getUsername());
            return join;
        } else if (playerUsernameDTO.getUsername().equals("err")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Player newPlayer = playerService.createPlayer(playerUsernameDTO);
        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(newPlayer.getAuthToken());
        response.setName(newPlayer.getUsername() + "'s lobby");
        response.setUsername(newPlayer.getUsername());
        return response;
    }

    @PutMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO joinLobby(@PathVariable long id, @RequestBody PlayerUsernameDTO playerUsernameDTO) {

        // dummy response
        if (id == 201L) {
            LobbyJoinDTO join = new LobbyJoinDTO();
            join.setToken(UUID.randomUUID().toString());
            join.setName(playerUsernameDTO.getUsername() + "'s lobby");
            join.setUsername(playerUsernameDTO.getUsername());
            return join;
        } else if (id == 400L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (id == 403L) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else if (id == 404L) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (id == 409L) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Player newPlayer = playerService.createPlayerInLobby(id, playerUsernameDTO);
        Lobby newPlayersLobby = lobbyService.getLobbyFromLobbyId(id);
        LobbyJoinDTO response = new LobbyJoinDTO();
        response.setToken(newPlayer.getAuthToken());
        response.setName(newPlayer.getUsername() + "'s lobby");
        response.setUsername(newPlayer.getUsername());
        return response;
    }
}
