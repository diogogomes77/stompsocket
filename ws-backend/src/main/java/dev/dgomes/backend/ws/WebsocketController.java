package dev.dgomes.backend.ws;

import dev.dgomes.backend.ws.model.WsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;

@Controller
public class WebsocketController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @MessageMapping("/send/all")
    @SendTo("/topic/public")
    public WsMessage sendMessage(@Payload WsMessage message) {
        System.out.println(message);
        return message;
    }

}