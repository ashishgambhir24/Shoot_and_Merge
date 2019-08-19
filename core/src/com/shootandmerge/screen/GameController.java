package com.shootandmerge.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.shootandmerge.config.GameConfig;
import com.shootandmerge.entity.DataCell;
import com.shootandmerge.entity.GameBackground;
import com.shootandmerge.entity.InputBar;
import com.shootandmerge.entity.MainBackground;
import com.shootandmerge.entity.UpperBound;

public class GameController {

    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    private UpperBound bound;
    private InputBar bar;
    private int inputValue;
    private DataCell inputCell;
    private int cellAffectedOnClickX;
    private Array<DataCell> affectedCells = new Array<>();
    private Pool<DataCell> cellPool;
    private int inputCounter =0;
    private float inputTimer=0;

    private int score =0;
    private int highScore = 0;
    private float mergeTimer=0f;

    private MainBackground mainBackground;
    private GameBackground gameBackground;


    private boolean start = false;

    private Array<Array<DataCell>> displayGrid = new Array<>();
    private Array<Float> gridYLevel = new Array<>();

    public GameController() {
        init();
    }

    private void init(){
        bound = new UpperBound();
        bar = new InputBar();
        inputCell = bar.getInputCell();
        for(int i = 0 ; i< GameConfig.INPUT_CELLS ; i++){
            Array<DataCell> displayColumn = new Array<>();
            displayGrid.add(displayColumn);
            gridYLevel.add(GameConfig.MAX_GAME_Y);
        }
        cellPool = Pools.get(DataCell.class , 50);

        mainBackground=new MainBackground();
        mainBackground.setLowerPosition(0,0);
        mainBackground.setUpperPosition(0 , GameConfig.MAX_GAME_Y);
        mainBackground.setSize(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT - GameConfig.MAX_GAME_Y);

        gameBackground = new GameBackground();
        gameBackground.setPosition(0 , GameConfig.MIN_GAME_INPUT_Y);
        gameBackground.setSize(GameConfig.WORLD_WIDTH , GameConfig.MAX_GAME_Y - GameConfig.MIN_GAME_INPUT_Y);

    }

    public UpperBound getBound() {
        return bound;
    }

    public InputBar getBar() {
        return bar;
    }

    public DataCell getInputCell() {
        return inputCell;
    }

    public int getInputValue() {
        return inputValue;
    }

    public Array<Array<DataCell>> getDisplayGrid() {
        return displayGrid;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public boolean isStart() {
        return start;
    }

    public MainBackground getMainBackground() {
        return mainBackground;
    }

    public GameBackground getGameBackground() {
        return gameBackground;
    }

    public void update(float delta){
            if(isGameOver() || !start){
                playAgain();
            }else {

                if (bar.getInputCell().getValue() == 0) {
                    createInputCell();
                }
                updateUpperBound(delta);
                moveDisplayGrid();
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    bar.setY(Gdx.input.getX() * (GameConfig.WORLD_WIDTH / GameConfig.WIDTH));
                    bar.setY(Gdx.input.getY() * (GameConfig.WORLD_HEIGHT / GameConfig.HEIGHT));
                }
                inputTimer+=delta;
                if(affectedCells.isEmpty() && inputTimer>=GameConfig.MIN_INPUT_TIME_GAP) {
                    cellAffectedOnClickX = clickedInputBar();

                    if ((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT ) || Gdx.input.justTouched()) && cellAffectedOnClickX != -1) {
                        updateDisplayGrid(cellAffectedOnClickX , delta);
                    }
                }else if (!affectedCells.isEmpty()){
                    updateDisplayGrid(affectedCells.removeIndex(0) , delta);
                }
                updateHighScore();
            }

    }

    public boolean isGameOver(){
        for(Array<DataCell> cellColumn : displayGrid){
            if(cellColumn.size>0) {
                DataCell lastCell = cellColumn.get(cellColumn.size - 1);
                if (lastCell.getY() < GameConfig.MIN_GAME_Y && affectedCells.isEmpty()) {
                    return true;
                }
            }else if(bound.getY()<GameConfig.MIN_GAME_Y){
                return true;
            }
        }
        return false;
    }

    private void createInputCell (){
        int randomPower;
        if(inputCounter<10){
            randomPower = MathUtils.random(1,3);
        }else if(inputCounter<20){
            randomPower = MathUtils.random(1,5);
        }else {
            randomPower = MathUtils.random(1,6);
        }
        int value = (int) Math.pow((double) 2, (double) randomPower);
        inputCell.setValue(value);
        inputValue = value;
    }

    private void updateUpperBound(float delta){
        bound.update(delta);
    }

    private int clickedInputBar(){

        int cellClicked = bar.clickedBlock();
        if((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT ) || Gdx.input.justTouched()) && cellClicked!=-1){

            DataCell newCell = cellPool.obtain();
            newCell.setPosition(GameConfig.X_BOUNDARY + cellClicked*GameConfig.CELL_WIDTH , GameConfig.MIN_GAME_Y);
            newCell.setDisplayGridX(cellClicked);
            newCell.setDisplayGridY(displayGrid.get(cellClicked).size);
            newCell.setValue(inputCell.getValue());

            displayGrid.get(cellClicked).add(newCell);
            updateGridYLevel();
            inputCounter++;
            inputCell.setValue(0);
            inputValue =0 ;
            inputTimer=0;
        }


        return cellClicked;
    }

    private void updateGridYLevel(){
        for(int i = 0 ; i<GameConfig.INPUT_CELLS ; i++){
            float yLevel = bound.getY() - displayGrid.get(i).size * GameConfig.CELL_HEIGHT;
            gridYLevel.set(i, yLevel);
        }
    }

    private void moveDisplayGrid(){
        for(int i = 0 ; i< GameConfig.INPUT_CELLS ; i++){
            int columnSize = displayGrid.get(i).size;
            for(int j =0 ; j< columnSize ;j++){
                DataCell cell = displayGrid.get(i).get(j);
                float maxCellY = bound.getY() - (j+1)*GameConfig.CELL_HEIGHT;
                cell.update(maxCellY);
                if(cell.getY() >= gridYLevel.get(i)-0.1 && !cell.isBoundDirectionSwitch()){
                    bound.setDirectionSwitchNewCell(true);
                    cell.setBoundDirectionSwitch(true);
                }
            }
        }
    }

    private void updateHighScore(){
        if(score>highScore){
            highScore = score;
        }
    }

    private void updateDisplayGrid(int cellAffectedX , float delta){
        int cellAffectedY = displayGrid.get(cellAffectedX).size-1;
        DataCell cellAffected = displayGrid.get(cellAffectedX).get(cellAffectedY);
        affectedCells.add(cellAffected);
        updateDisplayGrid(cellAffected , delta);
    }

    private void updateDisplayGrid (DataCell cellAffected , float delta) {
        int i = cellAffected.getDisplayGridX();
        int j = cellAffected.getDisplayGridY();

        int switchCase=-1;
        boolean aboveCellSame = j>0 && cellAffected.getValue()==displayGrid.get(i).get(j-1).getValue();
        boolean leftCellSame = i>0 && displayGrid.get(i-1).size>j && cellAffected.getValue()==displayGrid.get(i-1).get(j).getValue();
        boolean rightCellSame = i<GameConfig.INPUT_CELLS-1 && displayGrid.get(i+1).size>j && cellAffected.getValue()==displayGrid.get(i+1).get(j).getValue();
//        boolean leftToleftCellSame = i>1 && displayGrid.get(i-2).size>j && cellAffected.getValue()==displayGrid.get(i-2).get(j).getValue();
//        boolean rightToroghtCellSame = i<GameConfig.INPUT_CELLS-2 && displayGrid.get(i+2).size>j && cellAffected.getValue()==displayGrid.get(i+2).get(j).getValue();

        if(aboveCellSame && leftCellSame && rightCellSame){

            switchCase=1;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i-1).size>j+1){affectedCells.add(displayGrid.get(i-1).get(j+1));}
            if(displayGrid.get(i+1).size>j+1){affectedCells.add(displayGrid.get(i+1).get(j+1));}

        }else if(aboveCellSame && leftCellSame){

            switchCase=2;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i-1).size>j+1){affectedCells.add(displayGrid.get(i-1).get(j+1));}

        }else if(aboveCellSame && rightCellSame){

            switchCase=3;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i+1).size>j+1){affectedCells.add(displayGrid.get(i+1).get(j+1));}

        }else if (leftCellSame && rightCellSame){

            switchCase=4;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i-1).size>j+1 && displayGrid.get(i-1).get(j+1).getValue()!=(displayGrid.get(i).get(j).getValue()*4)){affectedCells.add(displayGrid.get(i-1).get(j+1));}
            if(displayGrid.get(i+1).size>j+1 && displayGrid.get(i+1).get(j+1).getValue()!=(displayGrid.get(i).get(j).getValue()*4)){affectedCells.add(displayGrid.get(i+1).get(j+1));}

        }else if (aboveCellSame){

            switchCase = 5;
            affectedCells.add(displayGrid.get(i).get(j));

        }else if (leftCellSame){

            switchCase = 6;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i-1).size>j+1 && displayGrid.get(i-1).get(j+1).getValue()!=(displayGrid.get(i).get(j).getValue()*2 )){affectedCells.add(displayGrid.get(i-1).get(j+1));}

        }else if(rightCellSame){

            switchCase = 7;
            affectedCells.add(displayGrid.get(i).get(j));
            if(displayGrid.get(i+1).size>j+1 && displayGrid.get(i+1).get(j+1).getValue()!=(displayGrid.get(i).get(j).getValue()*2)){affectedCells.add(displayGrid.get(i+1).get(j+1));}

        }



        if(cellAffected.getY()>=gridYLevel.get(i)-0.15) {
            mergeTimer+=delta;
            if(mergeTimer>=GameConfig.MERGE_TIMER) {
                DataCell cellRemoved;

                if (switchCase == 1) {
                    score += displayGrid.get(i).get(j).getValue() * 8;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 8);
                    displayGrid.get(i).get(j).setDisplayGridY(j - 1);
                    cellRemoved =displayGrid.get(i).removeIndex(j - 1);
                    cellPool.free(cellRemoved);

                    if (displayGrid.get(i - 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i - 1).size; k++) {
                            displayGrid.get(i - 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i - 1).removeIndex(j);
                    cellPool.free(cellRemoved);

                    if (displayGrid.get(i + 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i + 1).size; k++) {
                            displayGrid.get(i + 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i + 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);

                } else if (switchCase == 2) {
                    score += displayGrid.get(i).get(j).getValue() * 4;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 4);
                    displayGrid.get(i).get(j).setDisplayGridY(j - 1);
                    cellRemoved =displayGrid.get(i).removeIndex(j - 1);
                    cellPool.free(cellRemoved);

                    if (displayGrid.get(i - 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i - 1).size; k++) {
                            displayGrid.get(i - 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i - 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);

                } else if (switchCase == 3) {
                    score += displayGrid.get(i).get(j).getValue() * 4;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 4);
                    displayGrid.get(i).get(j).setDisplayGridY(j - 1);
                    cellRemoved =displayGrid.get(i).removeIndex(j - 1);
                    cellPool.free(cellRemoved);

                    if (displayGrid.get(i + 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i + 1).size; k++) {
                            displayGrid.get(i + 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i + 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);

                } else if (switchCase == 4) {
                    score += displayGrid.get(i).get(j).getValue() * 4;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 4);

                    if (displayGrid.get(i - 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i - 1).size; k++) {
                            displayGrid.get(i - 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i - 1).removeIndex(j);
                    cellPool.free(cellRemoved);

                    if (displayGrid.get(i + 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i + 1).size; k++) {
                            displayGrid.get(i + 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i + 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);

                } else if (switchCase == 5) {

                    score += displayGrid.get(i).get(j).getValue() * 2;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 2);
                    displayGrid.get(i).get(j).setDisplayGridY(j - 1);
                    cellRemoved =displayGrid.get(i).removeIndex(j - 1);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);

                } else if (switchCase == 6) {
                    score += displayGrid.get(i).get(j).getValue() * 2;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 2);
                    if (displayGrid.get(i - 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i - 1).size; k++) {
                            displayGrid.get(i - 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i - 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);
                    score += displayGrid.get(i).get(j).getValue();

                } else if (switchCase == 7) {
                    score += displayGrid.get(i).get(j).getValue() * 2;
                    displayGrid.get(i).get(j).setValue(displayGrid.get(i).get(j).getValue() * 2);
                    if (displayGrid.get(i + 1).size > j + 1) {
                        for (int k = j + 1; k < displayGrid.get(i + 1).size; k++) {
                            displayGrid.get(i + 1).get(k).setDisplayGridY(k - 1);
                        }
                    }
                    cellRemoved =displayGrid.get(i + 1).removeIndex(j);
                    cellPool.free(cellRemoved);
                    bound.setDirectionSwitchMerging(true);
                }
                mergeTimer = 0;
            }
        }
    }

    private void playAgain(){
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) || Gdx.input.justTouched()){
            score =0;
            inputCounter=0;
            mergeTimer=0;
            bound.setY(GameConfig.MAX_GAME_Y);
            start=true;
            restart();
        }
    }

    private void restart(){
        for(int i =0 ;i<GameConfig.INPUT_CELLS ; i++){
            cellPool.freeAll(displayGrid.get(i));
            displayGrid.get(i).clear();
        }
    }
}

