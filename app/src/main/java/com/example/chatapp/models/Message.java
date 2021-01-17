package com.example.chatapp.models;

public class Message {
    //    fields
    private final String messageText;

    //    constructor
    public Message(String messageText) {
        this.messageText = messageText;
    }
    //   methods

    public String getMessageText() {
        return messageText;
    }
}
