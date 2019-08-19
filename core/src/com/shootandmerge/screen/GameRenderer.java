package com.shootandmerge.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shootandmerge.assets.AssetsPath;
import com.shootandmerge.config.GameConfig;
import com.shootandmerge.entity.DataCell;
import com.shootandmerge.entity.InputBar;
import com.shootandmerge.entity.UpperBound;
import com.shootandmerge.util.GdxUtils;
import com.shootandmerge.util.ViewportUtils;
import com.shootandmerge.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private SpriteBatch batch;
    private BitmapFont valueFont;
    private BitmapFont scoreFont;
    private BitmapFont highScoreFont;

    private Texture gameOverTexture;
    private Texture startTexture;
    private Texture mainBackgroundTexture;
    private Texture gameBackgroundTexture;

    private Texture greenCell;
    private Texture cyanCell;
    private Texture orangeCell;
    private Texture pinkCell;
    private Texture purpleCell;
    private Texture darkRedCell;
    private Texture darkBlueCell;
    private Texture yellowCell;
    private Texture redCell;
    private Texture darkGreenCell;
    private Texture violetCell;
    private Texture lightBlueCell;
    private Texture emptyCell;




    private final GlyphLayout layout = new GlyphLayout();

    private DebugCameraController debugCameraController;

    private GameController controller;

    public GameRenderer(GameController controller) {
        this.controller = controller;
        init();
    }

    private void init(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT , camera);
        renderer= new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH , GameConfig.HUD_HEIGHT , hudCamera);
        batch = new SpriteBatch();
        valueFont = new BitmapFont(Gdx.files.internal(AssetsPath.VALUE_FONT));
        scoreFont = new BitmapFont(Gdx.files.internal(AssetsPath.SCORE_FONT));
        highScoreFont = new BitmapFont(Gdx.files.internal(AssetsPath.HIGH_SCORE_FONT));

        gameOverTexture = new Texture(Gdx.files.internal(AssetsPath.GAMEOVER_TEXTURE));
        startTexture = new Texture(Gdx.files.internal(AssetsPath.START_TEXTURE));
        mainBackgroundTexture = new Texture(Gdx.files.internal(AssetsPath.MAIN_BACKGROUND));
        gameBackgroundTexture = new Texture(Gdx.files.internal(AssetsPath.GAME_BACKGROUND));

        greenCell = new Texture(Gdx.files.internal(AssetsPath.GREEN_CELL));
        cyanCell = new Texture(Gdx.files.internal(AssetsPath.CYAN_CELL));
        orangeCell = new Texture(Gdx.files.internal(AssetsPath.ORANGE_CELL));
        pinkCell = new Texture(Gdx.files.internal(AssetsPath.PINK_CELL));
        purpleCell = new Texture(Gdx.files.internal(AssetsPath.PURPLE_CELL));
        darkRedCell = new Texture(Gdx.files.internal(AssetsPath.DARK_RED_CELL));
        darkBlueCell = new Texture(Gdx.files.internal(AssetsPath.DARK_BLUE_CELL));
        yellowCell = new Texture(Gdx.files.internal(AssetsPath.YELLOW_CELL));
        redCell = new Texture(Gdx.files.internal(AssetsPath.RED_CELL));
        darkGreenCell = new Texture(Gdx.files.internal(AssetsPath.DARK_GREEN_CELL));
        violetCell = new Texture(Gdx.files.internal(AssetsPath.VIOLET_CELL));
        lightBlueCell = new Texture(Gdx.files.internal(AssetsPath.LIGHT_BLUE_CELL));
        emptyCell = new Texture(Gdx.files.internal(AssetsPath.EMPTY_CELL));

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);



    }

    public void render(float delta){
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if(Gdx.input.justTouched()){
            Vector2 screenTouch = new Vector2(Gdx.input.getX() , Gdx.input.getY());
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));
            controller.getBar().setX(worldTouch.x);
            controller.getBar().setY(GameConfig.WORLD_HEIGHT - worldTouch.y);
        }

        GdxUtils.clearScreen();

        renderGameplay();

        renderUI();

        renderBorder();

        renderEndgame();

//        renderDebug();
    }

    public void resize(int width , int height){
        viewport.update(width,height,true);
        hudViewport.update(width,height,true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    private void renderGameplay(){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(mainBackgroundTexture ,
                controller.getMainBackground().getLowerX(),
                controller.getMainBackground().getLowerY(),
                controller.getMainBackground().getWidth(),
                controller.getMainBackground().getHeight());

        batch.draw(gameBackgroundTexture,
                controller.getGameBackground().getX(),
                controller.getGameBackground().getY(),
                controller.getGameBackground().getWidth(),
                controller.getGameBackground().getHeight());



        DataCell boundCell = new DataCell();
        UpperBound bound = controller.getBound();
        for (int i = 0 ; i<GameConfig.INPUT_CELLS ; i++){
            InputBar bar = controller.getBar();
            DataCell cell = bar.getInputCells(i);
            batch.draw(emptyCell , cell.getX() , cell.getY() , cell.getWidth() , cell.getHeight());
            for (int j = 0 ; j< GameConfig.MAX_GAME_Y - GameConfig.MIN_GAME_INPUT_Y ; j++){
                boundCell.setPosition(GameConfig.X_BOUNDARY + i*GameConfig.CELL_WIDTH,bound.getY() + j*GameConfig.CELL_HEIGHT);
                batch.draw(emptyCell , boundCell.getX() , boundCell.getY() , boundCell.getWidth() , boundCell.getHeight());
            }
        }

        batch.draw(mainBackgroundTexture ,
                controller.getMainBackground().getUpperX(),
                controller.getMainBackground().getUpperY(),
                controller.getMainBackground().getWidth(),
                controller.getMainBackground().getHeight());

        DataCell inputCell = controller.getInputCell();

        batch.draw(textureDecider(inputCell) , inputCell.getX() , inputCell.getY() , inputCell.getWidth() , inputCell.getHeight());

        for(Array<DataCell> cellColumn : controller.getDisplayGrid()){
            for (DataCell cell :cellColumn){
                batch.draw(textureDecider(cell) , cell.getX() , cell.getY() , cell.getWidth() , cell.getHeight());
            }
        }




        batch.end();
    }

    private void renderUI(){
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String scoreText = "" + controller.getScore();
        layout.setText(scoreFont , scoreText);
        scoreFont.draw(batch , scoreText , (GameConfig.HUD_WIDTH - layout.width)/2f , 6*(GameConfig.HUD_HEIGHT + layout.height)/7f);

        String highScoreText = "Best: " + controller.getHighScore();
        layout.setText(highScoreFont , highScoreText);
        highScoreFont.draw(batch , highScoreText , 4*(GameConfig.HUD_WIDTH - layout.width)/5f , 6*(GameConfig.HUD_HEIGHT + layout.height)/7f);

        if(controller.getInputValue() != 0 ){
            String valueText = "" + controller.getInputValue();
            layout.setText(valueFont , valueText);
            float cellX = controller.getInputCell().getX()*GameConfig.HUD_WORLD_X_RATIO;
            float cellY = controller.getInputCell().getY()*GameConfig.HUD_WORLD_Y_RATIO;
            float cellWidth = controller.getInputCell().getWidth()*GameConfig.HUD_WORLD_X_RATIO;
            float cellHeight = controller.getInputCell().getHeight()*GameConfig.HUD_WORLD_Y_RATIO;

            valueFont.draw(batch ,valueText ,  cellX + (cellWidth - layout.width)/2f , cellY + (cellHeight + layout.height)/2f);

        }

        for(Array<DataCell> cellColumn : controller.getDisplayGrid()){
            for (DataCell cell :cellColumn){
                float cellX = cell.getX()*GameConfig.HUD_WORLD_Y_RATIO;
                float cellY = cell.getY()*GameConfig.HUD_WORLD_Y_RATIO;
                float cellWidth = cell.getWidth()*GameConfig.HUD_WORLD_X_RATIO;
                float cellHeight = cell.getHeight()*GameConfig.HUD_WORLD_Y_RATIO;

                String valueText = "" + cell.getValue();
                layout.setText(valueFont , valueText);
                valueFont.draw(batch , valueText , cellX + (cellWidth - layout.width)/2f , cellY + (cellHeight + layout.height)/2f);
            }
        }

        batch.end();
    }

    private void renderEndgame(){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if(!controller.isStart()){
            batch.draw(startTexture ,
                    (GameConfig.WORLD_WIDTH - GameConfig.START_TEXTURE_WIDTH)/2f ,
                    (GameConfig.WORLD_HEIGHT - GameConfig.START_TEXTURE_HEIGHT)/2f,
                    GameConfig.START_TEXTURE_WIDTH,
                    GameConfig.START_TEXTURE_HEIGHT);
        }

        if(controller.isGameOver()) {
            batch.draw(gameOverTexture,
                    (GameConfig.WORLD_WIDTH - GameConfig.GAMEOVER_TEXTURE_WIDTH) / 2f,
                    (GameConfig.WORLD_HEIGHT - GameConfig.GAMEOVER_TEXTURE_HEIGHT) / 2f,
                    GameConfig.GAMEOVER_TEXTURE_WIDTH,
                    GameConfig.GAMEOVER_TEXTURE_HEIGHT);
        }

        batch.end();
    }

    private void renderDebug(){
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        ViewportUtils.drawGrid(viewport,renderer);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

    }

    private void drawDebug(){
        renderer.setColor(Color.GREEN);
        for(Array<DataCell> cellColumn : controller.getDisplayGrid()){
            for (DataCell cell :cellColumn){
                cell.drawDebug(renderer);
            }
        }

        renderer.setColor(Color.YELLOW);
        UpperBound bound = controller.getBound();
        bound.drawDebug(renderer);

        renderer.setColor(Color.YELLOW);
        InputBar bar = controller.getBar();
        bar.drawDebug(renderer);
    }

    private void renderBorder(){
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rectLine(0 , GameConfig.MIN_GAME_INPUT_Y , GameConfig.WORLD_WIDTH , GameConfig.MIN_GAME_INPUT_Y , 0.02f);
        renderer.rectLine(0 , GameConfig.MAX_GAME_Y , GameConfig.WORLD_WIDTH , GameConfig.MAX_GAME_Y , 0.02f);
        renderer.end();

    }

    private Texture textureDecider(DataCell cell){
        if (cell.getValue() == 2){
            return greenCell;
        }else if(cell.getValue() == 4){
            return cyanCell;
        }else if(cell.getValue() == 8){
            return orangeCell;
        }else if(cell.getValue() == 16){
            return pinkCell;
        }else if(cell.getValue() == 32){
            return purpleCell;
        }else if(cell.getValue() == 64){
            return darkRedCell;
        }else if(cell.getValue() == 128){
            return darkBlueCell;
        }else if(cell.getValue() == 256){
            return yellowCell;
        }else if(cell.getValue() == 512){
            return redCell;
        }else if(cell.getValue() == 1024){
            return darkGreenCell;
        }else if(cell.getValue() == 2048){
            return violetCell;
        }else if(cell.getValue() != 0){
            return lightBlueCell;
        }
        return emptyCell;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        valueFont.dispose();
    }
}
