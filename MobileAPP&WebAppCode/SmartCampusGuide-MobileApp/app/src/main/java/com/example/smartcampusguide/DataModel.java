package com.example.smartcampusguide;

public class DataModel {
    String head,body,photoUrl;

    public DataModel() {
    }

    public DataModel(String head, String body, String photoUrl) {
        this.head = head;
        this.body = body;
        this.photoUrl = photoUrl;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
