package com.shootandmerge.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.shootandmerge.config.GameConfig;

public class UpperBound {

    private float y = GameConfig.MAX_GAME_Y;
    private boolean directionSwitchMerging=false;
    private boolean directionSwitchNewCell =false;

    public UpperBound(){

    }

    public void drawDebug (ShapeRenderer renderer){
        renderer.line(GameConfig.X_BOUNDARY , y , GameConfig.WORLD_WIDTH - GameConfig.X_BOUNDARY , y);
    }

    public float getY(){
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setDirectionSwitchMerging(boolean directionSwitchMerging) {
        this.directionSwitchMerging = directionSwitchMerging;
    }

    public void setDirectionSwitchNewCell(boolean directionSwitchNewCell) {
        this.directionSwitchNewCell = directionSwitchNewCell;
    }

    public void update(float delta){
        float yspeed = GameConfig.UPPER_BOUND_DOWN_Y_SPEED*delta;
        if(directionSwitchMerging){
            yspeed = GameConfig.UPPER_BOUND_UP_Y_SPEED_MERGING;
            directionSwitchMerging=false;
        }else if(directionSwitchNewCell){
            yspeed = GameConfig.UPPER_BOUND_UP_Y_SPEED_NEW_CELL;
            directionSwitchNewCell=false;
        }
        y+=yspeed;
        y = MathUtils.clamp (y , GameConfig.MIN_GAME_Y , GameConfig.MAX_GAME_Y);
    }
}
