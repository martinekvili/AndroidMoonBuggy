package hu.bme.androidhazi.moonbuggy.game;

import android.content.Context;
import android.graphics.Canvas;

import org.json.JSONObject;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public class RenderLoop extends Thread {
    private static final long FPS = 30;
    private static final long timeBetweenFrames = 1000 / FPS;

    private final GameView view;
    private Game game;

    private volatile boolean running = false;
    private volatile boolean paused = false;

    private long lastProjectileTime;

    private void sleepThread(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private long getTime() {
        return System.currentTimeMillis();
    }

    public RenderLoop(Context context, GameView view) {
        this.view = view;
    }

    public void setDimensions(int width, int height){
        game.setDimensions(width, height);
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setGame(Game game) {
        this.game = game;
        game.startMusic();
    }

    @Override
    public void run() {
        while (running) {
            long renderStart = getTime();

            if (!paused) {
                game.step();
                draw();
            }

            long renderEnd = getTime();
            long sleepTime = timeBetweenFrames - (renderEnd - renderStart);
            if (sleepTime > 0) {
                sleepThread(sleepTime);
            } else {
                sleepThread(5);
            }
        }

        game.close();
    }

    private void draw() {
        Canvas c = null;
        try {
            c = view.getHolder().lockCanvas();

            if (c == null) {
                return;
            }

            synchronized (view.getHolder()) {
                game.draw(c);
            }
        } finally {
            if (c != null) {
                view.getHolder().unlockCanvasAndPost(c);
            }
        }
    }

    public void jump() {
        game.jump();
    }

    public void fire() {
        long currentTime = getTime();
        if (currentTime - lastProjectileTime > 200) {
            game.fire();
            lastProjectileTime = currentTime;
        }
    }

    public JSONObject getGameState() {
        return game.serialize();
    }
}
