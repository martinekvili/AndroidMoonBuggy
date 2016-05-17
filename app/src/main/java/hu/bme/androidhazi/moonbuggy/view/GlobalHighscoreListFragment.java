package hu.bme.androidhazi.moonbuggy.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Arrays;
import java.util.Map;

import hu.bme.androidhazi.moonbuggy.data.HighscoreViewModel;

/**
 * Created by vilmo on 2016. 05. 17..
 */
public class GlobalHighscoreListFragment extends HighscoreListFragmentBase {
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void fetchHighscores() {
        if (!isNetworkAvailable()) {
            progressBar.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
            return;
        }

        noInternet.setVisibility(View.GONE);

        Firebase ref = new Firebase("https://crackling-fire-5643.firebaseio.com/").child("highscores");
        Query query = ref.orderByChild("score");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                HighscoreViewModel[] highscores = new HighscoreViewModel[count];

                for (DataSnapshot score : dataSnapshot.getChildren()) {
                    highscores[--count] = new HighscoreViewModel((Map<String, Object>) score.getValue());
                }

                setListAdapter(Arrays.asList(highscores));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
