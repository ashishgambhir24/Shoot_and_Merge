package com.shootandmerge.config;

public class GameConfig {

    public static final float WIDTH= 480.0f;
    public static final float HEIGHT= 880.0f;

    public static final float HUD_WIDTH = 480.0f;
    public static final float HUD_HEIGHT = 880.0f;

    public static final float WORLD_WIDTH= 6.0f;
    public static final float WORLD_HEIGHT= 11.0f;

    public static final float HUD_WORLD_X_RATIO = HUD_WIDTH/WORLD_WIDTH;
    public static final float HUD_WORLD_Y_RATIO = HUD_HEIGHT/WORLD_HEIGHT;

    public static final float WORLD_CENTER_X= WORLD_WIDTH/2f;
    public static final float WORLD_CENTER_Y= WORLD_HEIGHT/2f;

    public static final float CELL_WIDTH = 1f;
    public static final float CELL_HEIGHT = 1f;

    public static final int INPUT_CELLS = 5;

    public static final float MIN_GAME_INPUT_Y = 1f;
    public static final float MIN_GAME_Y = MIN_GAME_INPUT_Y + CELL_HEIGHT;
    public static final float MAX_GAME_Y = WORLD_HEIGHT - 2*CELL_HEIGHT;
    public static final float X_BOUNDARY =(float) 0.5*CELL_WIDTH;
    public static final float UPPER_BOUND_DOWN_Y_SPEED = -0.06f;
    public static final float UPPER_BOUND_UP_Y_SPEED_MERGING = 0.1f;
    public static final float UPPER_BOUND_UP_Y_SPEED_NEW_CELL = 0.05f;

    public static final float CELL_UP_SPEED = 0.3f;
    public static final float MERGE_TIMER = 0.25f;
    public static final float MIN_INPUT_TIME_GAP = 0.6f;

    public static final float GAMEOVER_TEXTURE_HEIGHT = 3.0f;
    public static final float GAMEOVER_TEXTURE_WIDTH = 5.0f;

    public static final float START_TEXTURE_WIDTH = 3.0f;
    public static final float START_TEXTURE_HEIGHT = 4.5f;


    private GameConfig() {}
}
