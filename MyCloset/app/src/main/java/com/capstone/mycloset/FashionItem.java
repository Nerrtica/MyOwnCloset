package com.capstone.mycloset;

import android.graphics.Bitmap;

public class FashionItem {
    private Bitmap image;
    private String title;
    private String summary;
    private int color;

    public FashionItem(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public FashionItem(String title, String summary, Bitmap image) {
        this(title, summary);
        this.image = image;
    }

    public FashionItem(String title, String summary, Bitmap image, int color) {
        this(title, summary);
        this.image = image;
        this.color = color;
    }


    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getColor() { return color; }
}
