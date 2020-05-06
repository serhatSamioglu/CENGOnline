package com.example.cengonline.Classes;

public class User {
    private String eMail;
    private String password;
    private String id;
    private String type;

    public User(String eMail,String password,String id,String type){
        this.eMail = eMail;
        this.password = password;
        this.id = id;
        this.type = type;
    }

    public User(){

    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
