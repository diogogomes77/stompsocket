package dev.dgomes.backend.api;

import dev.dgomes.backend.ws.model.WsMessage;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Set;

import static dev.dgomes.backend.ws.model.WsMessage.MessageType.JOIN;


@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1"})
@RestController
public class WebController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired private SimpUserRegistry simpUserRegistry;

    @RequestMapping(value = "/welcome", method = RequestMethod.POST)
    public PostResponse Test(@RequestBody PostRequest inputPayload) {
        PostResponse response = new PostResponse();

        response.setMessage("Hello " + inputPayload.getName());

        JSONArray users = new JSONArray();
        Set<SimpUser> wsusers = simpUserRegistry.getUsers();
        for (SimpUser user : wsusers) {
            users.add(user.getName());
        }

        System.out.println("users: " + users.toString());
        response.setExtra(users.toString());

        String hiMessage = "Hi Everyone! I'm " + inputPayload.getName() + " \\o";

        WsMessage message = new WsMessage();
        message.setSender(inputPayload.getName());
        message.setContent(hiMessage);
        message.setRecipient("All");
        message.setType(JOIN);

        webSocket.convertAndSend("/topic/public", message);
        System.out.println(hiMessage);

        return response;
    }

}
