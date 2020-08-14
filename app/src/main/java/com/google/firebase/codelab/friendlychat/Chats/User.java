package com.google.firebase.codelab.friendlychat.Chats;

public class User {
    private String username;
    private String id;
    private String imageURL;


    public User(String name, String id, String photoUrl) {
        this.username = name;
        this.id = id;
        this.imageURL = photoUrl;
    }
    public User(String name, String photoUrl) {
        this.username = name;
        this.id = "id";
        this.imageURL = photoUrl;
    }


    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}
