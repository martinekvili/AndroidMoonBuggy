package hu.bme.androidhazi.moonbuggy.game;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import hu.bme.androidhazi.moonbuggy.GameActivity;
import hu.bme.androidhazi.moonbuggy.R;

/**
 * Created by vilmo on 2016. 05. 14..
 */
public class GameView extends SurfaceView {
    private final Context context;
    private GameActivity activity;

    private RenderLoop renderLoop;
    private Game game;

    private int width;
    private int height;

    private AlertDialog pauseDialog = null;
    private boolean isGameOver = false;

    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void stopRenderLoop() {
        boolean retry = true;
        renderLoop.setRunning(false);
        while (retry) {
            try {
                renderLoop.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(GameActivity activity, final String gameState) {
        this.activity = activity;

        if (gameState != null) {
            try {
                JSONObject jsonObject = new JSONObject(gameState);

                this.isGameOver = jsonObject.getBoolean("isGameOver");
            } catch (JSONException e) {
                Log.e("MOON_BUGGY_GAME", e.getMessage());
            }
        }

        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            private boolean gameCreated;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (isGameOver) {
                    return;
                }

                gameCreated = false;
                if (game == null) {
                    game = new Game(context, GameView.this, gameState);
                    gameCreated = true;
                }

                if (renderLoop == null || renderLoop.getState() != Thread.State.NEW) {
                    renderLoop = new RenderLoop(context, GameView.this);
                    renderLoop.setGame(game);
                }

                if (!gameCreated || gameState != null) {
                    pauseGame();
                }

                renderLoop.setRunning(true);
                renderLoop.start();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (renderLoop != null) {
                    stopRenderLoop();
                }

                if (pauseDialog != null) {
                    pauseDialog.dismiss();
                    pauseDialog = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                GameView.this.height = height;
                GameView.this.width = width;

                if (renderLoop != null && gameCreated && gameState == null) {
                    renderLoop.setDimensions(width, height);
                }
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() <= width / 3 && event.getY() >= height / 2) {
                    renderLoop.jump();
                }

                if (event.getX() >= 2 * width / 3 && event.getY() >= height / 2) {
                    renderLoop.fire();
                }

                if (event.getX() >= 7 * width / 8 && event.getY() <= height / 4) {
                    if (pauseDialog == null) {
                        pauseGame();
                    }
                }

                return true;
            }
        });
    }

    private void pauseGame() {
        renderLoop.setPaused(true);

        if (pauseDialog == null) {
            pauseDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.pauseGameTitle)
                    .setMessage(R.string.pauseGameMessage)
                    .setNegativeButton(R.string.pauseGameExitMenu, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.exitGame();
                        }
                    })
                    .setPositiveButton(R.string.pauseGameResume, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            renderLoop.setPaused(false);
                            pauseDialog = null;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .create();
            pauseDialog.setCanceledOnTouchOutside(false);
            pauseDialog.setCancelable(false);
            pauseDialog.show();
        }
    }

    public void gameOver() {
        isGameOver = true;
        renderLoop.setRunning(false);

        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                activity.exitGame(game.getPoints());
            }
        });
    }

    public String getGameState() {
        JSONObject jsonObject;

        if (renderLoop != null) {
            stopRenderLoop();

            jsonObject = renderLoop.getGameState();
        } else {
            jsonObject = new JSONObject();
        }

        try {

            jsonObject.put("isGameOver", isGameOver);
        } catch (JSONException e) {
            Log.e("MOON_BUGGY_GAME", e.getMessage());
        }

        return jsonObject.toString();
    }
}
