package hu.bme.androidhazi.moonbuggy.view;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import hu.bme.androidhazi.moonbuggy.data.HighscoreViewModel;
import hu.bme.androidhazi.moonbuggy.data.orm.HighscoreEntity;

/**
 * Created by vilmo on 2016. 05. 17..
 */
public class LocalHighscoreListFragment extends HighscoreListFragmentBase {
    @Override
    protected void fetchHighscores() {
        new GetLocalHighscoresTask().execute();
    }

    private class GetLocalHighscoresTask extends AsyncTask<Void, Void, List<HighscoreViewModel>> {

        @Override
        protected List<HighscoreViewModel> doInBackground(Void... params) {
            List<HighscoreEntity> highscoreEntities = HighscoreEntity.find(HighscoreEntity.class, null, null, null, "score DESC", null);

            List<HighscoreViewModel> highscores = new ArrayList<>();
            for (HighscoreEntity entity : highscoreEntities) {
                highscores.add(new HighscoreViewModel(entity));
            }
            return highscores;
        }

        @Override
        protected void onPostExecute(List<HighscoreViewModel> highscoreViewModels) {
            setListAdapter(highscoreViewModels);
        }
    }
}
