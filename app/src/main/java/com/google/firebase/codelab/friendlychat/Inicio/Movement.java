package com.google.firebase.codelab.friendlychat.Inicio;

class Movement {
    private  String key;
    private String value;
    public  Movement(String key, String value){


        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
