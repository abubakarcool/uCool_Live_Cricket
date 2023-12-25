package com.example.samplestickerapp.Models;

public class Link {
    private String name, logo, num;

    public Link() {
    }

    public Link(String name, String logo, String num) {
        this.name = name;
        this.logo = logo;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
