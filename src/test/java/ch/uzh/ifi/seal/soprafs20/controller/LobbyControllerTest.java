package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.constant.GameLength;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.incoming.LobbySettingsDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyPlayerDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.LobbyStateDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Disabled("Disabled, needs additional work")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LobbyControllerTest {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private PlayerService playerService;
    @MockBean
    private LobbyService lobbyService;

    @SpyBean private LobbyController lobbyController;

    WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        doReturn(true).when(lobbyController)
                .checkSender(Mockito.nullable(String.class), Mockito.nullable(String.class));
    }

    @Test
    public void testChangeLobbySettings() throws Exception {
        BlockingQueue<LobbyStateDTO> bq = new LinkedBlockingDeque<>();

        LobbySettingsDTO lobbySettings = generateLobbySettings();
        LobbyStateDTO lobbyState = generateLobbyState();

        when(lobbyService.updateLobbySettings(Mockito.any(), Mockito.any())).thenReturn(lobbyState);

        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
        session.subscribe("/user/queue/lobby/a/lobby-state", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return LobbyStateDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((LobbyStateDTO) payload);
            }
        });

        session.send("/app/lobby/a/settings", lobbySettings);

        LobbyStateDTO response = bq.poll(2, SECONDS);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(lobbyState.getPlayers(), response.getPlayers());
        Assertions.assertEquals(lobbyState.getSettings(), response.getSettings());
    }


    private LobbySettingsDTO generateLobbySettings() {
        LobbySettingsDTO s = new LobbySettingsDTO();
        s.setPublicLobby(true);
        s.setLobbyName("lobby");
        s.setDuration(GameLength.MEDIUM);
        return s;
    }

    private LobbyStateDTO generateLobbyState() {
        LobbyStateDTO l = new LobbyStateDTO();
        LobbySettingsDTO s = new LobbySettingsDTO();
        s.setDuration(GameLength.MEDIUM);
        s.setLobbyName("lobby");
        s.setPublicLobby(true);
        LobbyPlayerDTO p = new LobbyPlayerDTO();
        p.setAdmin(true);
        p.setUsername("player");
        l.setSettings(s);
        l.setPlayers(new LobbyPlayerDTO[]{p});
        return l;
    }
}