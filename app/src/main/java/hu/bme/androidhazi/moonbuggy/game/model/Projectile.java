package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Color;
import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public class Projectile extends GameObject {

    public Projectile(Game game) {
        super(game);
        paint.setColor(Color.RED);
    }

    public Projectile(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);
        paint.setColor(Color.RED);
    }

    @Override
    public void step() {
        xPos += width / 2;

        if (xPos > screenWidth) {
            game.removeObject(this);
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        this.width = width / 32;
        this.height = height / 72;
    }

    public void adjustPosition(Buggy buggy) {
        Rect buggyPos = buggy.getBoundingRect();

        this.xPos = buggyPos.right;
        this.yPos = buggyPos.top + buggyPos.height() / 4;
    }

    @Override
    public void checkCollisions(List<IGameObject> gameObjects) {
        for (IGameObject object : gameObjects) {
            if (object.doIHitProjectile(this)) {
                game.removeObject(object);
                game.removeObject(this);
                game.incrementPoints();

                return;
            }
        }
    }

    @Override
    protected void putData(JSONObject jsonObject) throws JSONException {
        jsonObject.put("type", "projectile");
    }
}
