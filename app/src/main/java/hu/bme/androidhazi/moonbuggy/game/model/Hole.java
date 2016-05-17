package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public class Hole extends GroundBase {

    private boolean didJumpOver = false;

    public Hole(int startNumber, Game game) {
        super(startNumber, game);
    }

    public Hole(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);

        this.didJumpOver = jsonObject.getBoolean("didJumpOver");
    }

    @Override
    protected float getHeight(int height) {
        return height / 36;
    }

    @Override
    protected float getYPos(int height) {
        return height * 35 / 36;
    }

    @Override
    public boolean doIKillBuggy(Buggy buggy) {
        Rect buggyPos = buggy.getBoundingRect();

        if (!didJumpOver && buggyPos.right > xPos + width) {
            didJumpOver = true;
            game.incrementPoints();
        }

        return !buggy.isJumping() &&
                buggyPos.left >= xPos &&
                buggyPos.right <= xPos + width;
    }

    @Override
    protected void putData(JSONObject jsonObject) throws JSONException {
        jsonObject.put("type", "hole");
        jsonObject.put("didJumpOver", didJumpOver);
    }
}
