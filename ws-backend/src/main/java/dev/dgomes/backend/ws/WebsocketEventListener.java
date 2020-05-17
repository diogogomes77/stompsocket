package dev.dgomes.backend.ws;

import dev.dgomes.backend.ws.model.WsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static dev.dgomes.backend.ws.model.WsMessage.MessageType.LEAVE;

@Component
public class WebsocketEventListener {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("new socket connected");

        /*
        WsMessage message = new WsMessage();
        message.setType(WsMessage.MessageType.JOIN);
        message.setRecipient("All");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        //String username = (String) headerAccessor.getLogin();
        System.out.println("Login: " + username);

        if(username != null) {
            message.setSender(username);
            message.setContent("Hi Everyone! I'm " + username);
        }
        webSocket.convertAndSend("/topic/public", message);
         */
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("socket disconnected");
        WsMessage message = new WsMessage();
        message.setType(LEAVE);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            message.setSender(username);
            message.setRecipient("All");
            message.setContent("Goodbye!");
        }
        webSocket.convertAndSend("/topic/public", message);
    }
}
