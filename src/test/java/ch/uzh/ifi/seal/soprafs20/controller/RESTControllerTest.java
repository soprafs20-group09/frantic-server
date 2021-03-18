package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Lobby;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyJoinDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyListElementDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerScoreDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerUsernameDTO;
import ch.uzh.ifi.seal.soprafs20.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.doNothing;
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

    @MockBean
    private RegisterService registerService;

    @MockBean
    private GameService gameService;

    @Test
    void getLobbies_returnsLobbyList() throws Exception {

        LobbyListElementDTO lobby = new LobbyListElementDTO();
        lobby.setLobbyId("1");
        lobby.setName("foo");
        lobby.setCreator("bar");
        lobby.setRunning(false);
        lobby.setRoundCount(0);

        List<LobbyListElementDTO> allLobbies = Collections.singletonList(lobby);
        given(lobbyService.getLobbies(Mockito.any())).willReturn(allLobbies);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(lobby.getName())))
                .andExpect(jsonPath("$[0].creator", is(lobby.getCreator())))
                .andExpect(jsonPath("$[0].running", is(lobby.isRunning())))
                .andExpect(jsonPath("$[0].roundCount", is(lobby.getRoundCount())));
    }

    @Test()
    void post_validUsername_returnsAuthToken() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername("foo");

        LobbyJoinDTO lobbyJoinDTO = new LobbyJoinDTO();
        lobbyJoinDTO.setName("foo's lobby");
        lobbyJoinDTO.setUsername("foo");

        doNothing().when(lobbyService).checkLobbyCreate(Mockito.any());
        given(registerService.prepareLobby(Mockito.any())).willReturn(lobbyJoinDTO);

        // when
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(lobbyJoinDTO.getName())))
                .andExpect(jsonPath("$.username", is(lobbyJoinDTO.getUsername())));
    }

    @Test()
    void post_invalidUsername_throwsException() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername(" ");

        Player player = new Player();
        player.setId(1L);
        player.setUsername(" ");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username missing or invalid.")).when(lobbyService).checkLobbyCreate(Mockito.any());

        // when
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(postRequest).andExpect(status().isBadRequest());
    }

    @Test
    void put_validUsername_returnsAuthToken() throws Exception {

        PlayerUsernameDTO username = new PlayerUsernameDTO();
        username.setUsername("foo");

        LobbyJoinDTO lobbyJoinDTO = new LobbyJoinDTO();
        lobbyJoinDTO.setName("foo's lobby");
        lobbyJoinDTO.setUsername("foo");

        doNothing().when(lobbyService).checkLobbyJoin(Mockito.any(), Mockito.any());
        given(registerService.prepareLobby(Mockito.any(), Mockito.any())).willReturn(lobbyJoinDTO);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(username));
        // then
        mockMvc.perform(putRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(lobbyJoinDTO.getName())))
                .andExpect(jsonPath("$.username", is(lobbyJoinDTO.getUsername())));
    }

    @Test
    void put_privateLobby_throwsException() throws Exception {

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