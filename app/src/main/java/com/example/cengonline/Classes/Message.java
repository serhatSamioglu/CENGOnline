package com.example.cengonline.Classes;

public class Message {
    private String message;
    private String sender;
    private Long date;

    public Message(String message, String sender, Long date) {
        this.message = message;
        this.sender = sender;
        this.date = date;
    }

    public Message(){
        //Firebase databaseden mesaj verilerini obje olarak almak için
        //Message.class özelliği sağlıyor.
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
