package com.example.myapplication.ModelClass;

public class ListItem {

    private String itemId;
    private String picture;
    private String title;
    private String category;
    private String price;
    private String availableFor;
    private  String uploader;
    private String date;
    private String condition;
    private String status;
    private String description;
    public ListItem(String itemId,String picture,String title,String description, String category, String price,String availableFor,String uploader,String date,String condition, String status)
    {
        this.itemId = itemId;
        this.category = category;
        this.title = title;
        this.price=price;
        this.picture=picture;
        this.availableFor=availableFor;
        this.uploader = uploader;
        this.date = date;
        this.condition = condition;
        this.status = status;
        this.description=description;
    }

    public String getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }


    public String getCategory() {
        return category;
    }



    public String getPrice() {
        return price;
    }

    public String getAvailableFor() {
        return availableFor;
    }

    public String getItemId() {
        return itemId;
    }

    public String getUploader() {
        return uploader;
    }

    public String getDate() {
        return date;
    }

    public String getCondition() {
        return condition;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
