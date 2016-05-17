package hu.bme.androidhazi.moonbuggy.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.bme.androidhazi.moonbuggy.R;
import hu.bme.androidhazi.moonbuggy.game.model.Buggy;
import hu.bme.androidhazi.moonbuggy.game.model.GameObject;
import hu.bme.androidhazi.moonbuggy.game.model.Ground;
import hu.bme.androidhazi.moonbuggy.game.model.GroundBase;
import hu.bme.androidhazi.moonbuggy.game.model.Hole;
import hu.bme.androidhazi.moonbuggy.game.model.IGameObject;
import hu.bme.androidhazi.moonbuggy.game.model.Projectile;
import hu.bme.androidhazi.moonbuggy.game.model.SpaceShip;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public class Game {
    private final Context context;
    private final GameView gameView;

    private int width;
    private int height;

    private Buggy buggy;
    private List<IGameObject> gameObjects;

    private List<IGameObject> toRemove;
    private List<IGameObject> toAdd;

    private Paint background;
    private Paint textColor;
    private Bitmap pauseIcon;

    private Random random;
    private boolean lastIsHole;
    private int stepUntilNextSpaceShip;

    private int points;

    private MediaPlayer mediaPlayer;
    private int currentPositionInMusic;

    public Game(Context context, GameView gameView, String gameState) {
        this.context = context;
        this.gameView = gameView;
        random = new Random();

        background = new Paint();
        background.setColor(Color.BLACK);
        textColor = new Paint();
        textColor.setColor(Color.WHITE);
        textColor.setTextAlign(Paint.Align.CENTER);
        textColor.setTextSize(20.0f * context.getResources().getDisplayMetrics().density);
        textColor.setFakeBoldText(true);
        pauseIcon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_media_pause);

        gameObjects = new ArrayList<>();
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();

        if (gameState == null) {
            this.lastIsHole = false;
            this.stepUntilNextSpaceShip = 0;
            this.points = 0;

            this.buggy = new Buggy(this);
            gameObjects.add(buggy);

            for (int i = 0; i <= GroundBase.widthDivider; i++) {
                GroundBase ground = new Ground(i, this);
                gameObjects.add(ground);
            }

            this.currentPositionInMusic = 0;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(gameState);

                this.width = jsonObject.getInt("width");
                this.height = jsonObject.getInt("height");

                this.lastIsHole = jsonObject.getBoolean("lastIsHole");
                this.stepUntilNextSpaceShip = jsonObject.getInt("stepUntilNextSpaceShip");
                this.points = jsonObject.getInt("points");

                this.buggy = (Buggy) GameObject.deserialize(this, jsonObject.getJSONObject("buggy"));
                gameObjects.add(buggy);

                JSONArray jsonObjects = jsonObject.getJSONArray("gameObjects");
                for (int i = 0; i < jsonObjects.length(); i++) {
                    gameObjects.add(GameObject.deserialize(this, jsonObjects.getJSONObject(i)));
                }

                this.currentPositionInMusic = jsonObject.getInt("currentPositionInMusic");

            } catch (JSONException e) {
                Log.e("MOON_BUGGY_GAME", e.getMessage());
            }
        }
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("width", width);
            jsonObject.put("height", height);

            jsonObject.put("lastIsHole", lastIsHole);
            jsonObject.put("stepUntilNextSpaceShip", stepUntilNextSpaceShip);
            jsonObject.put("points", points);

            jsonObject.put("buggy", buggy.getJson());

            JSONArray jsonObjects = new JSONArray();
            for (IGameObject object : gameObjects) {
                if (object != buggy) {
                    jsonObjects.put(object.getJson());
                }
            }
            jsonObject.put("gameObjects", jsonObjects);

            jsonObject.put("currentPositionInMusic", currentPositionInMusic);

        } catch (JSONException e) {
            Log.e("MOON_BUGGY_GAME", e.getMessage());
        }

        return jsonObject;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;

        for (IGameObject object : gameObjects) {
            object.setDimensions(width, height);
        }
    }

    public void step() {
        if (stepUntilNextSpaceShip == 0) {
            if (random.nextDouble() >= 0.97) {
                SpaceShip ss = new SpaceShip(random.nextInt(6), this);
                ss.setDimensions(width, height);
                gameObjects.add(ss);

                stepUntilNextSpaceShip = 6;
            }
        } else {
            stepUntilNextSpaceShip--;
        }

        for (IGameObject object : gameObjects) {
            object.step();
        }

        for (IGameObject object : gameObjects) {
            object.checkCollisions(gameObjects);
        }

        gameObjects.removeAll(toRemove);
        toRemove.clear();

        synchronized (toAdd) {
            gameObjects.addAll(toAdd);
            toAdd.clear();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, background);

        canvas.drawBitmap(pauseIcon, width * 15 / 16 - pauseIcon.getWidth() / 2, height / 8 - pauseIcon.getHeight() / 2, null);
        canvas.drawText(Integer.toString(points), width / 16, height / 8, textColor);

        for (IGameObject object : gameObjects) {
            object.draw(canvas);
        }
    }

    public void fire() {
        Projectile projectile = new Projectile(this);
        projectile.setDimensions(width, height);
        projectile.adjustPosition(buggy);

        synchronized (toAdd) {
            toAdd.add(projectile);
        }
    }

    public void jump() {
        buggy.jump();
    }

    public void removeGround(GroundBase groundBase) {
        removeObject(groundBase);

        GroundBase newGround;
        if (!lastIsHole && random.nextDouble() > 0.8) {
            newGround = new Hole(GroundBase.widthDivider, this);
            lastIsHole = true;
        } else {
            newGround = new Ground(GroundBase.widthDivider, this);
            lastIsHole = false;
        }
        newGround.setDimensions(width, height);

        synchronized (toAdd) {
            toAdd.add(newGround);
        }
    }

    public void removeObject(IGameObject object) {
        toRemove.add(object);
    }

    public void over() {
        gameView.gameOver();
    }

    public void incrementPoints() {
        points += 10;
    }

    public void close() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            currentPositionInMusic = mediaPlayer.getCurrentPosition();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startMusic() {
        int musicVolume = PreferenceManager.getDefaultSharedPreferences(context).getInt("music_volume", 100);

        if (musicVolume == 0) {
            return;
        }

        try {
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(context, Uri.parse("android.resource://hu.bme.androidhazi.moonbuggy/" + R.raw.endure));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.seekTo(currentPositionInMusic);
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setLooping(true);

            float log1 = (float)(Math.log(101 - musicVolume) / Math.log(101));
            mediaPlayer.setVolume(1 - log1, 1 - log1);

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("MOON_BUGGY_GAME", e.getMessage());
        }
    }

    public int getPoints() {
        return points;
    }
}
