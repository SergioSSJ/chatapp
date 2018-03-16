package org.sjimenez.chatapp.controllers;

import org.sjimenez.chatapp.pojo.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;


@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/send/message/{groupname}")
    public void onReceivedMesage(@DestinationVariable("groupname") String groupname,String message , Principal principal){

        System.out.println(groupname);
        System.out.println(message);
        this.template.convertAndSend("/chat/"+groupname,new ChatMessage(principal.getName(),message));
    }
}
