package hu.bme.androidhazi.moonbuggy.service;

import android.app.IntentService;
import android.content.Intent;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import hu.bme.androidhazi.moonbuggy.data.orm.HighscoreEntity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SaveHighscoreService extends IntentService {

    public SaveHighscoreService() {
        super("SaveHighscoreService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String name = intent.getStringExtra("name");
        int score = intent.getIntExtra("points", 0);

        new HighscoreEntity(name, score).save();

        Firebase ref = new Firebase("https://crackling-fire-5643.firebaseio.com/").child("highscores");

        Map<String, Object> highscore = new HashMap<>();
        highscore.put("name", name);
        highscore.put("score", Long.valueOf(score));

        ref.push().setValue(highscore);
    }
}
