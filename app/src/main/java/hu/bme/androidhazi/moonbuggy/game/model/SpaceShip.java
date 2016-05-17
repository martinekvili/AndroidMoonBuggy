package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public class SpaceShip extends GameObject {

    private final int heightPos;

    public SpaceShip(int heightPos, Game game) {
        super(game);
        paint.setColor(Color.GREEN);

        if (heightPos != 3) {
            this.heightPos = heightPos;
        } else {
            this.heightPos = 4;
        }
    }

    public SpaceShip(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);
        paint.setColor(Color.GREEN);

        this.heightPos = 0;
    }

    @Override
    public void step() {
        xPos -= width / 5;

        if (xPos <= -width) {
            game.removeObject(this);
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        this.width = width / 16;
        this.height = height / 18;

        this.xPos = width - this.width;
        this.yPos = height * (24 + heightPos) / 36;
    }

    @Override
    public boolean doIKillBuggy(Buggy buggy) {
        return getBoundingRect().intersect(buggy.getBoundingRect());
    }

    @Override
    public boolean doIHitProjectile(IGameObject projectile) {
        return getBoundingRect().intersect(projectile.getBoundingRect());
    }

    @Override
    protected void putData(JSONObject jsonObject) throws JSONException {
        jsonObject.put("type", "spaceShip");
    }
}
