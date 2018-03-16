package org.sjimenez.chatapp.pojo;

public class ChatMessage {

    private String name;
    private String message;

    public ChatMessage(String name,String message){
        this.name=name;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
