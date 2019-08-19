package com.shootandmerge.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.shootandmerge.config.GameConfig;

public class DataCell implements Pool.Poolable {


    private float x;
    private float y;
    private float width = GameConfig.CELL_WIDTH;
    private float height = GameConfig.CELL_HEIGHT;
    private int displayGridX;
    private int displayGridY;
    private boolean boundDirectionSwitch = false;

    private int value;

    private Rectangle cell;

    public DataCell(){
        cell = new Rectangle(x,y,width,height);
    }

    public void setPosition(float x, float y ){
        this.x = x;
        this.y = y;
        updateCell();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getDisplayGridX() {
        return displayGridX;
    }

    public void setDisplayGridX(int displayGridX) {
        this.displayGridX = displayGridX;
    }

    public int getDisplayGridY() {
        return displayGridY;
    }

    public void setDisplayGridY(int displayGridY) {
        this.displayGridY = displayGridY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isBoundDirectionSwitch() {
        return boundDirectionSwitch;
    }

    public void setBoundDirectionSwitch(boolean boundDirectionSwitch) {
        this.boundDirectionSwitch = boundDirectionSwitch;
    }

    private void updateCell(){
        cell.setPosition(x,y);
        cell.setSize(width , height);
    }

    public void update(float maxCellY){
        float ySpeed = GameConfig.CELL_UP_SPEED;
        y+=ySpeed;
        y= MathUtils.clamp(y , GameConfig.MIN_GAME_INPUT_Y , maxCellY);

        updateCell();

    }

    public void drawDebug(ShapeRenderer renderer){
        renderer.rect(cell.x , cell.y , cell.width , cell.height);
    }

    @Override
    public void reset() {
        boundDirectionSwitch=false;
        value=0;
    }
}
