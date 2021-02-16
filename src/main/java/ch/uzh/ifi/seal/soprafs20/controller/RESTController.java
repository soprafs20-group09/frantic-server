package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyListElementDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.RegisterService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides RESTful endpoints
 */
@RestController
public class RESTController {

    private final LobbyService lobbyService;
    private final RegisterService registerService;
    private final GameService gameService;

    RESTController(LobbyService lobbyService, RegisterService registerService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.registerService = registerService;
        this.gameService = gameService;
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyListElementDTO> getAllLobbies(@RequestParam(required = false) String q) {
        List<Lobby> lobbies = lobbyService.getLobbies(q);
        List<LobbyListElementDTO> response = new ArrayList<>();
        for (Lobby lobby : lobbies) {
            response.add(DTOMapper.INSTANCE.convertLobbyToLobbyListDTO(lobby));
        }
        return response;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameDTO> getAllGames() {
        return this.gameService.getGames();
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO createLobby(@Valid @RequestBody PlayerUsernameDTO dto) {

        String username = clean(dto.getUsername());
        lobbyService.checkLobbyCreate(username);
        return registerService.prepareLobby(username);
    }

    @PutMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyJoinDTO joinLobby(@PathVariable String id, @RequestBody PlayerUsernameDTO dto) {

        String username = clean(dto.getUsername());
        lobbyService.checkLobbyJoin(id, username);
        return registerService.prepareLobby(id, username);
    }

    private String clean(String arg) {
        return Jsoup.clean(arg, Whitelist.basic());
    }
}
