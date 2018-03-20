package org.sjimenez.chatapp.dao;

import org.sjimenez.chatapp.mappers.MessageMapper;
import org.sjimenez.chatapp.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDao {

    @Autowired
    MessageMapper messageMapper;

    public void insertMessage(Message message){
        messageMapper.insertMessage(message);
    }


}
