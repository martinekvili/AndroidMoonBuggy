package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Canvas;
import android.graphics.Rect;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public interface IGameObject {
    void step();
    void draw(Canvas canvas);

    void setDimensions(int width, int height);
    Rect getBoundingRect();

    void checkCollisions(List<IGameObject> gameObjects);
    boolean doIKillBuggy(Buggy buggy);
    boolean doIHitProjectile(IGameObject projectile);

    JSONObject getJson();
}
