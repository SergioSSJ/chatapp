package org.sjimenez.chatapp.controllers;

import org.sjimenez.chatapp.pojo.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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
        System.out.println(principal.getName());

    }

    //@SubscribeMapping( "/send/delete/{groupname}")
    @MessageMapping("/send/delete/{groupname}")
    public void onDeletedFromGroup(@DestinationVariable("groupname") String groupname,String user, Principal principal){
        System.out.println("Deleted from"+groupname+"user:"+user);
        this.template.convertAndSendToUser(principal.getName(),"/delete",user);
    }
}
