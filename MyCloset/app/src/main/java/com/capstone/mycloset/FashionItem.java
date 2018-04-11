package com.capstone.mycloset;

public class FashionItem {
    private int image;
    private String title;
    private String summary;

    public FashionItem(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public FashionItem(String title, String summary, int image) {
        this(title, summary);
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }
}
