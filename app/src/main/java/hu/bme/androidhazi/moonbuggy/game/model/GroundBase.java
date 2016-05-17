package hu.bme.androidhazi.moonbuggy.game.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import hu.bme.androidhazi.moonbuggy.game.Game;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public abstract class GroundBase extends GameObject {
    public static final int widthDivider = 10;

    private final int startNumber;

    public GroundBase(int startNumber, Game game) {
        super(game);
        paint.setColor(Color.WHITE);

        this.startNumber = startNumber;
    }

    public GroundBase(Game game, JSONObject jsonObject) throws JSONException {
        super(game, jsonObject);
        paint.setColor(Color.WHITE);

        this.startNumber = 0;
    }

    @Override
    public void step() {
        xPos -= width / 10;

        if (xPos <= -width + 1) {
            game.removeGround(this);
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        this.width = (float) Math.ceil((double) width / widthDivider);
        this.height = getHeight(height);

        this.xPos = startNumber * this.width;
        this.yPos = getYPos(height);
    }

    protected abstract float getHeight(int height);
    protected abstract  float getYPos(int height);
}
