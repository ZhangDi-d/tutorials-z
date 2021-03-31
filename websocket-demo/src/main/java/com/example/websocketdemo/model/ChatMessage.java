package com.example.websocketdemo.model;

/**
 * @author dizhang
 * @date 2021-03-16
 */
public class ChatMessage {

    private MessageType messageType;
    private String content;
    private String sender;

    public enum MessageType{
        JOIN,
        CHAT,
        LEAVE,
        ;
    }


    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
