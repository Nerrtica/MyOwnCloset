package com.capstone.mycloset;

public class BookmarkItem {
    private int image;
    private String title;

    public BookmarkItem(String title) {
        this.title = title;
    }

    public BookmarkItem(String title, int image) {
        this(title);
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
