package com.capstone.mycloset;

//closet 구조체이다.
class Closet {
    private int id;
    private int type;
    private int color;
    private int pattern;
    private int isLong;
    private String imagePath;
    public Closet(int id, int type, int pattern, int color, int isLong, String imagePath){
        this.id = id;
        this.type = type;
        this.pattern = pattern;
        this.color = color;
        this.isLong = isLong;
        this.imagePath = imagePath;
    }
    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }
    public int getPattern() {
        return pattern;
    }
    public int getColor() {
        return color;
    }
    public int isLong() {
        return isLong;
    }
    public String getImagePath() {
        return imagePath;
    }
}