package com.capstone.mycloset;

import android.graphics.Bitmap;

public class FashionItem {
    private Bitmap image;
    private String title;
    private String summary;

    public FashionItem(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public FashionItem(String title, String summary, Bitmap image) {
        this(title, summary);
        this.image = image;
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
}
