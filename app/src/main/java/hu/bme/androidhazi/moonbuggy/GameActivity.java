package hu.bme.androidhazi.moonbuggy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.androidhazi.moonbuggy.game.GameView;
import hu.bme.androidhazi.moonbuggy.service.SaveHighscoreService;
import hu.bme.androidhazi.moonbuggy.view.GameOverDialogFragment;

public class GameActivity extends AppCompatActivity implements GameOverDialogFragment.IGameOverDialogCallback {

    @BindView(R.id.gameView)
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey("gameState")) {
            gameView.init(this, savedInstanceState.getString("gameState"));
        } else {
            gameView.init(this, null);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("gameState", gameView.getGameState());

        super.onSaveInstanceState(outState);
    }

    public void exitGame(int points) {
        GameOverDialogFragment dialog = new GameOverDialogFragment();
        dialog.setPoints(points);

        dialog.show(getFragmentManager(), GameOverDialogFragment.TAG);
    }

    public void exitGame() {
        finish();
    }

    @Override
    public void sendName(String name, int points) {
        Intent saveHighscoreIntent = new Intent(this, SaveHighscoreService.class);
        saveHighscoreIntent.putExtra("name", name);
        saveHighscoreIntent.putExtra("points", points);
        startService(saveHighscoreIntent);

        finish();
    }
}
