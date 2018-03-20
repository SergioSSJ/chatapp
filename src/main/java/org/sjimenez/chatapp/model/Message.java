package org.sjimenez.chatapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Message {

    private int idmessage;
    private String content;
    private LocalDate creation;
    private int idgroup;
    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getIdmessage() {
        return idmessage;
    }

    public void setIdmessage(int idmessage) {
        this.idmessage = idmessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public int getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(int idgroup) {
        this.idgroup = idgroup;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return idmessage == message.idmessage &&
                idgroup == message.idgroup &&
                Objects.equals(content, message.content) &&
                Objects.equals(creation, message.creation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idmessage, content, creation, idgroup);
    }

    @Override
    public String toString() {
        return "Message{" +
                "idmessage=" + idmessage +
                ", content='" + content + '\'' +
                ", creation=" + creation +
                ", idgroup=" + idgroup +
                '}';
    }
}
