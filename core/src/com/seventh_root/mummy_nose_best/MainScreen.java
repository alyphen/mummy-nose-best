package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.TimerTask;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class MainScreen extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private Texture texture;
    private Board gameBoard;
    private java.util.Timer eventsTimer;
    PlayerManager playerManager;

    public MainScreen() {
        spriteBatch = new SpriteBatch();
        playerManager = new PlayerManager();
        gameBoard = new Board(6,10);
        gameBoard.create();
        compressBoard();

        texture = new Texture("tools.png");

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {

                tileClickListener((int)Math.floor(x / 64),(int)Math.floor(y / 64));
                // your touch down code here
                return true; // return true to indicate the event was handled
            }
        });

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                gameBoard.addAdditionalRows();

                compressBoard();
            }
        };

        eventsTimer = new java.util.Timer();
        eventsTimer.scheduleAtFixedRate(task, 0, 20000);
    }

    public void compressBoard() {
        boolean madeCompression = false;
        do {
            gameBoard.checkBoard();
            madeCompression = gameBoard.compressBoard();
        }while(madeCompression);
    }

    public void tileClickListener(int x, int y) {
        // Reverse y as it is from the bottom.
        int reverseY = 9 - y;
        // Make sure we're on the board.
        if (x >= this.gameBoard.getWidth() || reverseY > this.gameBoard.getHeight()) {
            return;
        }

        int tileNumber = this.gameBoard.getTile(x, reverseY);

        if (tileNumber > 0) {
            int swapDirection = (reverseY > 5) ? -1 : 1;

            if (swapDirection == 1) {

                for (int py = reverseY + 1; py < 10; py++) {
                    int foundTileValue = this.gameBoard.getTile(x, py);

                    if (foundTileValue > 0) {
                        if (py > 5) {
                            this.gameBoard.swapBlocks(x, reverseY, py);
                        }
                        else {
                            // we aren't on the bottom so rotate the blocks around
                            this.gameBoard.rotateBlocks(x,true);
                        }

                        // Get out of the loop as we've either done a swap or rejected the swap
                        break;
                    }
                }
            } else {

                for (int py = reverseY - 1; py >= 0; py--) {
                    int foundTileValue = this.gameBoard.getTile(x, py);

                    if (foundTileValue > 0) {
                        if (py < 5) {
                            this.gameBoard.swapBlocks(x, reverseY, py);
                        }
                        else {
                            // we aren't on the bottom so rotate the blocks around
                            this.gameBoard.rotateBlocks(x,false);
                        }
                        // Get out of the loop as we've either done a swap or rejected the swap
                        break;
                    }
                }
            }

            compressBoard();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();

        // draw the board... refactor!!!!
        for(int x=0;x<gameBoard.getWidth();x++)
        {
            for(int y=0;y<gameBoard.getHeight();y++)
            {
                int i = this.gameBoard.getTile(x,y);
                if(i!=0) {
                    spriteBatch.draw(texture, 64*x, 64*y, 64*(i-1), 0, 64, 64);
                }
            }
        }
        playerManager.renderPlayers(delta, spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        playerManager.dispose();
    }

}
