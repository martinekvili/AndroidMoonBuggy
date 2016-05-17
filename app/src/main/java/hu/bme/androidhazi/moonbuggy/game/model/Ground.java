package hu.bme.androidhazi.moonbuggy.game.model;

import org.json.JSONException;
import org.json.JSONObject;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public class Ground extends GroundBase {

    public Ground(int startNumber, Game game) {
        super(startNumber, game);
    }

    public Ground(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);
    }

    @Override
    protected float getHeight(int height) {
        return height / 9;
    }

    @Override
    protected float getYPos(int height) {
        return height * 8 / 9;
    }

    @Override
    protected void putData(JSONObject jsonObject) throws JSONException {
        jsonObject.put("type", "ground");
    }
}
