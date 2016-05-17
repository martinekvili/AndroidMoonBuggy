package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public class Buggy extends GameObject {

    private static final int maxJumpStep = 24;
    private int jumpStep;

    public Buggy(Game game) {
        super(game);
        paint.setColor(Color.CYAN);

        jumpStep = 0;
    }

    public Buggy(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);
        paint.setColor(Color.CYAN);

        this.jumpStep = jsonObject.getInt("jumpStep");
    }

    @Override
    public void step() {
        if (jumpStep > 0) {
            jumpStep--;

            yPos -= (2 * jumpStep + 1 - maxJumpStep) * width / 150;
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        this.width = width / 16;
        this.height = height / 9;
        this.xPos = width * 0.2f;
        this.yPos = height * 7 / 9;
    }

    public void jump() {
        if (jumpStep == 0) {
            jumpStep = maxJumpStep;
        }
    }

    @Override
    public void checkCollisions(List<IGameObject> gameObjects) {
        for (IGameObject object : gameObjects) {
            if (object.doIKillBuggy(this)) {
                game.over();
                return;
            }
        }
    }

    @Override
    protected void putData(JSONObject jsonObject) throws JSONException {
        jsonObject.put("type", "buggy");
        jsonObject.put("jumpStep", jumpStep);
    }

    public boolean isJumping() {
        return jumpStep != 0;
    }
}
