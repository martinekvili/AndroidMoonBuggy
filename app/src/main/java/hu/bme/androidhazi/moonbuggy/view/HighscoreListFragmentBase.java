package hu.bme.androidhazi.moonbuggy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.androidhazi.moonbuggy.R;
import hu.bme.androidhazi.moonbuggy.data.HighscoreViewModel;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public abstract class HighscoreListFragmentBase extends Fragment {

    @BindView(R.id.lblNoInternet)
    TextView noInternet;

    @BindView(R.id.highscoreProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.highscoreListLayout)
    LinearLayout linearLayout;

    @BindView(R.id.highscoreList)
    RecyclerView highscoreList;

    private HighscoreListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_highscorelist, container, false);

        ButterKnife.bind(this, layout);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        highscoreList.setHasFixedSize(true);

        // use a linear layout manager
        highscoreList.setLayoutManager(new LinearLayoutManager(getActivity()));

        listAdapter = new HighscoreListAdapter();
        highscoreList.setAdapter(listAdapter);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchHighscores();
    }

    protected abstract void fetchHighscores();

    protected void setListAdapter(List<HighscoreViewModel> highscores) {
        int number = 0;
        int lastScore = -1;
        for (HighscoreViewModel highscore : highscores) {
            if (lastScore != highscore.getScore()) {
                number++;
                lastScore = highscore.getScore();
            }

            highscore.setNumber(number);
        }

        listAdapter.setData(highscores);

        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
