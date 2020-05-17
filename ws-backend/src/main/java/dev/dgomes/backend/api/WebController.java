package dev.dgomes.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @RequestMapping("/sample")
    public SampleResponse Sample(@RequestParam(value = "name",
            defaultValue = "Robot") String name) {
        SampleResponse response = new SampleResponse();
        response.setId(1);
        response.setMessage("Your name is "+name);
        webSocket.convertAndSend("/topic/public", "Hello " + name + "!");
        return response;

    }

    @RequestMapping(value = "/welcome", method = RequestMethod.POST)
    public PostResponse Test(@RequestBody PostRequest inputPayload) {
        PostResponse response = new PostResponse();
        response.setId(inputPayload.getId()*100);
        response.setMessage("Hello " + inputPayload.getName());
        response.setExtra("Some text");
        webSocket.convertAndSend("/topic/public", "Hello " + inputPayload.getName() + "!");
        return response;
    }

}
