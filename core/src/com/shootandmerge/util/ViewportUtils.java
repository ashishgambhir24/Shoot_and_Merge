package com.shootandmerge.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shootandmerge.config.GameConfig;

/**
 * Created by goran on 22/08/2016.
 */
public class ViewportUtils {

    private static final Logger log = new Logger(ViewportUtils.class.getName(), Logger.DEBUG);

    private static final int DEFAULT_CELL_SIZE = 1;

    public static void drawGrid(Viewport viewport, ShapeRenderer renderer) {
        drawGrid(viewport, renderer, DEFAULT_CELL_SIZE);
    }

    public static void drawGrid(Viewport viewport, ShapeRenderer renderer, int cellSize) {
        // validate parameters/arguments
        if (viewport == null) {
            throw new IllegalArgumentException("viewport param is required.");
        }

        if (renderer == null) {
            throw new IllegalArgumentException("renderer param is required.");
        }

        if (cellSize < DEFAULT_CELL_SIZE) {
            cellSize = DEFAULT_CELL_SIZE;
        }

        // copy old color from render
        Color oldColor = new Color(renderer.getColor());

        int worldWidth = (int) viewport.getWorldWidth();
        int worldHeight = (int) viewport.getWorldHeight();
        int doubleWorldWidth = worldWidth * 2;
        int doubleWorldHeight = worldHeight * 2;

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);

        // draw vertical lines
        for (float x = -doubleWorldWidth + GameConfig.X_BOUNDARY; x < doubleWorldWidth - GameConfig.X_BOUNDARY; x += cellSize) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight);
        }

        // draw horizontal lines
        for (int y = -doubleWorldHeight; y < doubleWorldHeight; y += cellSize) {
            renderer.line(-doubleWorldWidth, y, doubleWorldWidth, y);
        }

        // draw x-y axis lines
        renderer.setColor(Color.RED);
        renderer.line(0, -doubleWorldHeight, 0, doubleWorldHeight);
        renderer.line(-doubleWorldWidth, 0, doubleWorldWidth, 0);


        // draw lower limit for player
        renderer.setColor(Color.PURPLE);
        renderer.line(GameConfig.X_BOUNDARY,GameConfig.MIN_GAME_INPUT_Y , worldWidth - GameConfig.X_BOUNDARY, GameConfig.MIN_GAME_INPUT_Y);
        renderer.line(GameConfig.X_BOUNDARY,GameConfig.MAX_GAME_Y , worldWidth - GameConfig.X_BOUNDARY, GameConfig.MAX_GAME_Y);

        renderer.line(GameConfig.X_BOUNDARY,GameConfig.MIN_GAME_INPUT_Y , GameConfig.X_BOUNDARY, GameConfig.MAX_GAME_Y);
        renderer.line(worldWidth - GameConfig.X_BOUNDARY,GameConfig.MIN_GAME_INPUT_Y , worldWidth - GameConfig.X_BOUNDARY, GameConfig.MAX_GAME_Y);

        renderer.end();

        renderer.setColor(oldColor);
    }

    public static void debugPixelPerUnit(Viewport viewport) {
        if (viewport == null) {
            throw new IllegalArgumentException("viewport param is required.");
        }

        float screenWidth = viewport.getScreenWidth();
        float screenHeight = viewport.getScreenHeight();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // PPU => pixels per world unit
        float xPPU = screenWidth / worldWidth;
        float yPPU = screenHeight / worldHeight;

        log.debug("x PPU= " + xPPU + " y PPU= " + yPPU);
    }

    private ViewportUtils() {
    }
}
