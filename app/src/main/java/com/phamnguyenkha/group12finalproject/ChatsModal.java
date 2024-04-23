package com.phamnguyenkha.group12finalproject;

public class ChatsModal {
    private String message;
    private String sender;

    public ChatsModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getType() {
        if (sender.equals("user")) {
            return ChatRVAdapter.VIEW_TYPE_USER;
        } else if (sender.equals("bot")) {
            return ChatRVAdapter.VIEW_TYPE_BOT;
        }
        return -1;
    }

}

