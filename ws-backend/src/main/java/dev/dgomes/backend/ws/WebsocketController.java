package dev.dgomes.backend.ws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    @MessageMapping("/send/message")
    @SendTo("/topic/public")
    public String sendMessage(String message){
        System.out.println(message);
        return message;
    }
}