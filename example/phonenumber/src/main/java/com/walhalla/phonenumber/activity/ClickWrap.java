package com.walhalla.phonenumber.activity;

public class ClickWrap {
    private int id = 0;
//    public float x;
//    public float y;

    public int x;
    public int y;

    public ClickWrap(int x, int y) {
        this.x = x;
        this.y = y;
    }
//    public ClickWrap(float x, float y) {
//        this.x = x;
//        this.y = y;
//    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}