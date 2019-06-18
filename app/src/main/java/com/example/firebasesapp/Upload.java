package com.example.firebasesapp;

public class Upload {
    private String name;
    private String imageUrl;
    public Upload(){}
    public Upload(String name,String imageUrl){
        if(name.trim().equals("")){
            name="No_name";
        }
        name=name;
        imageUrl=imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}