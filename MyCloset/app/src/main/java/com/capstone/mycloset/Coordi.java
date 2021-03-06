package com.capstone.mycloset;

class Coordi{
    private int id;
    private String title;
    private int outer_id;
    private int top_id;
    private int bottom_id;
    private int shoes_id;
    public Coordi(int id, String title, int outerWear, int top, int bottom, int shoes){
        this.id = id;
        this.title = title;
        this.outer_id = outerWear;
        this.top_id = top;
        this.bottom_id = bottom;
        this.shoes_id = shoes;
    }

    public Coordi(int id, String title, int top, int bottom, int shoes) {
        this.id = id;
        this.title = title;
        this.outer_id = -1;
        this.top_id = top;
        this.bottom_id = bottom;
        this.shoes_id = shoes;
    }

    public Coordi(int outer, int top, int bottom, int shoes) {
        this.outer_id = outer;
        this.top_id = top;
        this.bottom_id = bottom;
        this.shoes_id = shoes;
    }

    public Coordi(int top, int bottom, int shoes) {
        this.outer_id = -1;
        this.top_id = top;
        this.bottom_id = bottom;
        this.shoes_id = shoes;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getTop () {
        return top_id;
    }
    public int getBottom() {
        return bottom_id;
    }
    public int getShoes() {
        return  shoes_id;
    }
    public int getOuter(){
        return outer_id;
    }
}
