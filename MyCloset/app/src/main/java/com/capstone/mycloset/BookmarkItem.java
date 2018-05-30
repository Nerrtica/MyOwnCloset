package com.capstone.mycloset;

public class BookmarkItem {
    private int image;
    private int coordiID;
    private String title;

    public BookmarkItem(int coordiID, String title) {
        this.coordiID = coordiID;
        this.title = title;
    }

    public BookmarkItem(int coordiID, String title, int image) {
        this.coordiID = coordiID;
        this.title = title;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public int getCoordiID() {
        return coordiID;
    }

    public String getTitle() {
        return title;
    }
}
