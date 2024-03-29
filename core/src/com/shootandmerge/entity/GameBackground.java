package com.shootandmerge.entity;

public class GameBackground {
    private float x;
    private float y;
    private float width;
    private float height;

    public GameBackground() {
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setSize(float width , float height){
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
