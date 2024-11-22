package com.example.myapplication.ModelClass;

public class HistoryItem {
    private String id;
    private String title;
    private String type;
    private String status;
    private String imageUrl;

    public HistoryItem(String  id, String title, String type, String status, String imageUrl) {
        this.title = title;
        this.type = type;
        this.status = status;
        this.imageUrl = imageUrl;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
