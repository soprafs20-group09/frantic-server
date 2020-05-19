package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.service.LobbyService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.RegisterDTO;
import ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing.RegisteredDTO;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.when;

@Disabled("Disabled, functional, but needs addtional work")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterControllerTest {

    WebSocketStompClient stompClient;
    @Value("${local.server.port}")
    private int port;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private LobbyService lobbyService;
    @SpyBean
    private WebSocketController webSocketController;

    @Test
    void testRegistration() throws Exception {
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        BlockingQueue<RegisteredDTO> bq = new LinkedBlockingDeque<>();

        Player player = new Player();
        player.setUsername("player");
        player.setIdentity("player");

        RegisteredDTO registered = new RegisteredDTO("player", "lobby");

        // mocks
        //doReturn("player").when(webSocketController).checkAuthentication(Mockito.nullable(String.class));
        when(playerService.createPlayer(Mockito.any(), Mockito.any())).thenReturn(player);
        when(lobbyService.createLobby(Mockito.any())).thenReturn("lobby");
        when(playerService.registerPlayer(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(registered);

        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);
        session.subscribe("/user/queue/register", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RegisteredDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((RegisteredDTO) payload);
            }
        });

        RegisterDTO register = new RegisterDTO();
        register.setToken("a");
        session.send("/app/register", register);

        RegisteredDTO response = bq.poll(2, SECONDS);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(player.getUsername(), response.getUsername());
        Assertions.assertEquals(registered.getLobbyId(), response.getLobbyId());
    }
}