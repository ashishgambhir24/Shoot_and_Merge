package com.shootandmerge.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shootandmerge.config.GameConfig;


public class InputBar {

    private DataCell[] inputCells;
    private float X;
    private float Y;


    public InputBar() {
        inputCells = new DataCell[5];
        for (int i = 0 ; i< GameConfig.INPUT_CELLS ; i++){
            inputCells[i] = new DataCell();
            inputCells[i].setPosition(GameConfig.X_BOUNDARY + (i*GameConfig.CELL_WIDTH) , GameConfig.MIN_GAME_INPUT_Y);
        }
    }

    public void setX(float X) {
        this.X = X;
    }

    public void setY(float Y) {
        this.Y = Y;
    }

    public DataCell getInputCells(int i) {
        return inputCells[i];
    }

    public DataCell getInputCell(){
        return inputCells[GameConfig.INPUT_CELLS/2];
    }

    public void drawDebug(ShapeRenderer renderer){
        for (int i =0 ; i<GameConfig.INPUT_CELLS ; i++){
            inputCells[i].drawDebug(renderer);
        }
    }

    public int clickedBlock(){

        float inputYMin = GameConfig.WORLD_HEIGHT - GameConfig.MAX_GAME_Y;
        float inputYMax = GameConfig.WORLD_HEIGHT;
        float startCellX = GameConfig.X_BOUNDARY;
        float cellWidth = GameConfig.CELL_WIDTH;

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.justTouched()){
            if(Y>inputYMin && Y<inputYMax) {
                for(int i = 0 ; i<GameConfig.INPUT_CELLS ; i++){
                    if(X < startCellX + (i+1)*cellWidth && X > startCellX + i*cellWidth){
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
