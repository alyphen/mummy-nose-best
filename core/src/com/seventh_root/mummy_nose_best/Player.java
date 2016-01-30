package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Math.abs;

public class Player {

    private Board board;
    public final int index;
    public final Controller controller;
    public final Cursor cursor;

    public Player(Board board, int index, Controller controller) {
        this.board = board;
        this.index = index;
        this.controller = controller;
        this.cursor = new Cursor(this);
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        cursor.moveTo(cursor.x, (board.highestBottomRow - 1) * 64);
        if (abs(controller.getAxis(0)) >= 0.2F)
            cursor.move(controller.getAxis(0) * delta * 64, 0);
        cursor.render(delta, spriteBatch);
    }

    public void dispose() {
        cursor.dispose();
    }

}
