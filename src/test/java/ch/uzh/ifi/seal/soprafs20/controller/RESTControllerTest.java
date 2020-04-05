package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RestController
 * This is a WebMvcTest which allows to test the RESTController i.e. GET/POST/PUT request without actually sending them over the network.
 * This tests if the RESTController works.
 */
@WebMvcTest(RESTController.class)
public class RESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private PlayerService playerService;

    @Test
    public void getLobbies_returnsLobbyList() throws Exception {

        Lobby lobby = new Lobby();
        lobby.setLobbyId("1");
        lobby.setName("foo");
        lobby.setCreator("bar");
        lobby.setPlayers(3);

        List<Lobby> allLobbies = Collections.singletonList(lobby);
        given(lobbyService.getLobbies(Mockito.any())).willReturn(allLobbies);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(lobby.getName())))
                .andExpect(jsonPath("$[0].creator", is(lobby.getCreator())))
                .andExpect(jsonPath("$[0].players", is(lobby.getPlayers())));
    }

    @Test()
    public void post_validUsername_returnsAuthToken() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername("foo");

        Player player = new Player();
        player.setId(1L);
        player.setUsername("foo");

        given(playerService.createPlayer(Mockito.any(), Mockito.any())).willReturn(player);

        // when
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(player.getUsername() + "'s lobby")))
                .andExpect(jsonPath("$.username", is(player.getUsername())));
    }

    @Test
    public void getPlayersInLobby_returnsListOfPlayersInLobby() throws Exception {

        PlayerScoreDTO player = new PlayerScoreDTO();
        player.setUsername("foo");
        player.setScore(168);

        List<PlayerScoreDTO> allScores = Collections.singletonList(player);

        given(lobbyService.getScores(Mockito.any())).willReturn(allScores);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(player.getUsername())))
                .andExpect(jsonPath("$[0].score", is(player.getScore())));
    }

    @Test
    public void put_validUsername_returnsAuthToken() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername("foo");

        given(lobbyService.isUsernameAlreadyInLobby(Mockito.any(), Mockito.any())).willReturn(false);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(putRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(username.getUsername() + "'s lobby")))
                .andExpect(jsonPath("$.username", is(username.getUsername())));
    }

    @Test
    public void put_privateLobby_throwsException() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername("foo");

        Player player = new Player();
        player.setId(1L);
        player.setUsername("foo");

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(lobbyService).checkLobbyJoin(Mockito.any(), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(putRequest).andExpect(status().isForbidden());
    }

    /**
     * Helper Method to convert Object into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}