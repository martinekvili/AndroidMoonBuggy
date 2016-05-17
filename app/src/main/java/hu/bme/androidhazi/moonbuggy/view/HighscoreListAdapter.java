package hu.bme.androidhazi.moonbuggy.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.androidhazi.moonbuggy.R;
import hu.bme.androidhazi.moonbuggy.data.HighscoreViewModel;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public class HighscoreListAdapter extends RecyclerView.Adapter<HighscoreListAdapter.ViewHolder> {

    private List<HighscoreViewModel> highscores;

    public void setData(List<HighscoreViewModel> highscores) {
        this.highscores = highscores;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.highscorerow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HighscoreViewModel highscore = highscores.get(position);

        holder.number.setText(Integer.toString(highscore.getNumber()) + '.');
        holder.name.setText(highscore.getName());
        holder.score.setText(Integer.toString(highscore.getScore()));
    }

    @Override
    public int getItemCount() {
        return highscores == null ? 0 : highscores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.highscoreNumber)
        TextView number;

        @BindView(R.id.highscoreName)
        TextView name;

        @BindView(R.id.highscoreScore)
        TextView score;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            /*number = (TextView) itemView.findViewById(R.id.highscoreNumber);
            name = (TextView) itemView.findViewById(R.id.highscoreName);
            score = (TextView) itemView.findViewById(R.id.highscoreScore);*/
        }
    }
}
