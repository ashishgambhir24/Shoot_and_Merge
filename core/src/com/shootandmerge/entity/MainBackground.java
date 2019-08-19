package com.shootandmerge.entity;

public class MainBackground {
    private float upperX;
    private float upperY;
    private float lowerX;
    private float lowerY;
    private float width;
    private float height;

    public MainBackground() {
    }

    public void setUpperPosition(float upperX, float upperY){
        this.upperX = upperX;
        this.upperY = upperY;
    }

    public void setLowerPosition(float lowerX, float lowerY){
        this.lowerX = lowerX;
        this.lowerY = lowerY;
    }

    public void setSize(float width , float height){
        this.width = width;
        this.height = height;
    }

    public float getUpperX() {
        return upperX;
    }

    public float getUpperY() {
        return upperY;
    }

    public float getLowerX() {
        return lowerX;
    }

    public float getLowerY() {
        return lowerY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
