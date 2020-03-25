package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
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

    @Test
    public void getLobbies_returnsLobbyList() throws Exception {

        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setName("foo");
        lobby.setCreator("bar");
        lobby.setPlayers(3);

        List<Lobby> allLobbies = Collections.singletonList(lobby);
        given(lobbyService.getLobbies(Mockito.any(), Mockito.any())).willReturn(allLobbies);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk());
    }

    @Test
    public void createLobby_validUsername_returnsLobby() throws Exception {

        PlayerUsernameDTO player = new PlayerUsernameDTO();
        player.setUsername("foo");

        LobbyJoinDTO lobby = new LobbyJoinDTO();
        lobby.setName("foo");
        lobby.setToken("123");
        lobby.setUsername("foo");

        given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(player));
        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is("123")))
                .andExpect(jsonPath("$.name", is("foo")))
                .andExpect(jsonPath("$.username", is("foo")));
    }

    @Test
    public void getPlayersInLobby_returnsListOfPlayersInLobby() throws Exception {

        PlayerScoreDTO player = new PlayerScoreDTO();
        player.setUsername("foo");
        player.setScore(168);

        List<PlayerScoreDTO> allScores = Collections.singletonList(player);

        given(lobbyService.getScores(Mockito.anyLong())).willReturn(allScores);

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
    public void joinLobby_validUsername_returnsLobby() throws Exception {

        PlayerUsernameDTO player = new PlayerUsernameDTO();
        player.setUsername("foo");

        LobbyJoinDTO lobby = new LobbyJoinDTO();
        lobby.setName("foo");
        lobby.setToken("123");
        lobby.setUsername(player.getUsername());

        given(lobbyService.joinLobby(Mockito.anyLong(), Mockito.any())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(player));
        // then
        mockMvc.perform(putRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(lobby.getToken())))
                .andExpect(jsonPath("$.name", is(lobby.getName())))
                .andExpect(jsonPath("$.username", is(lobby.getUsername())));
    }

    @Test
    public void joinLobby_privateLobby_throwsException() throws Exception {

        PlayerUsernameDTO player = new PlayerUsernameDTO();
        player.setUsername("foo");

        given(lobbyService.joinLobby(Mockito.anyLong(), Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(player));
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