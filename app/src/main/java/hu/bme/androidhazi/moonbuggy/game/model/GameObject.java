package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public abstract class GameObject implements IGameObject {
    protected int screenWidth;
    protected int screenHeight;

    protected float xPos;
    protected float yPos;

    protected float width;
    protected float height;

    protected final Paint paint;

    protected final Game game;

    public GameObject(Game game) {
        this.game = game;

        paint = new Paint();
    }

    public GameObject(Game game, JSONObject jsonObject) throws JSONException {
        this(game);

        this.screenWidth = jsonObject.getInt("screenWidth");
        this.screenHeight = jsonObject.getInt("screenHeight");
        this.xPos = (float) jsonObject.getDouble("xPos");
        this.yPos = (float) jsonObject.getDouble("yPos");
        this.width = (float) jsonObject.getDouble("width");
        this.height = (float) jsonObject.getDouble("height");
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(xPos, yPos, xPos + width, yPos + height, paint);
    }

    @Override
    public void setDimensions(int width, int height) {
        screenHeight = height;
        screenWidth = width;
    }

    @Override
    public Rect getBoundingRect() {
        return new Rect((int)xPos, (int)yPos, (int)(xPos + width), (int)(yPos + height));
    }

    @Override
    public void checkCollisions(List<IGameObject> gameObjects) {
    }

    @Override
    public boolean doIKillBuggy(Buggy buggy) {
        return false;
    }

    @Override
    public boolean doIHitProjectile(IGameObject projectile) {
        return false;
    }

    @Override
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            putData(jsonObject);

            jsonObject.put("screenWidth", screenWidth);
            jsonObject.put("screenHeight", screenHeight);
            jsonObject.put("xPos", xPos);
            jsonObject.put("yPos", yPos);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
        } catch (JSONException e) {
            Log.e("MOON_BUGGY_GAME", e.getMessage());
        }

        return jsonObject;
    }

    protected abstract void putData(JSONObject jsonObject) throws JSONException;

    public static IGameObject deserialize(Game game, JSONObject jsonObject) throws JSONException {
        switch (jsonObject.getString("type")) {
            case "buggy":
                return new Buggy(game, jsonObject);
            case "ground":
                return new Ground(game, jsonObject);
            case "hole":
                return new Hole(game, jsonObject);
            case "projectile":
                return new Projectile(game, jsonObject);
            case "spaceShip":
                return new SpaceShip(game, jsonObject);
            default:
                return null;
        }
    }
}
