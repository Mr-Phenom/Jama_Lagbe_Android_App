package com.example.myapplication.ModelClass;

public class ListItem {

    private String picture;
    private String title;
    private String category;
    private String price;
    private String availableFor;
    public ListItem(String picture,String title, String category, String price,String availableFor)
    {
        this.category = category;
        this.title = title;
        this.price=price;
        this.picture=picture;
        this.availableFor=availableFor;
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
}
