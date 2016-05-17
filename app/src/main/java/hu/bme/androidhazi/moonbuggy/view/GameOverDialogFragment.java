package hu.bme.androidhazi.moonbuggy.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.androidhazi.moonbuggy.R;

/**
 * Created by vilmo on 2016. 05. 15..
 */
public class GameOverDialogFragment extends DialogFragment {

    public static final String TAG = "GameOverDialogFragment";

    public interface IGameOverDialogCallback {
        void sendName(String name, int points);
    }

    private SharedPreferences preferences;

    private IGameOverDialogCallback callback;

    @BindView(R.id.tbName)
    EditText tbName;

    private int points;

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the IGameOverDialogCallback so we can send events to the host
            callback = (IGameOverDialogCallback) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement IGameOverDialogCallback");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("points")) {
            setPoints(savedInstanceState.getInt("points"));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_gameover, null);

        ButterKnife.bind(this, dialogView);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        tbName.setText(preferences.getString("last_used_name", ""));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(getString(R.string.gameOverTitle))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(getString(R.string.gameOverMessage, points))
                .setView(dialogView)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = tbName.getText().toString();

                        preferences.edit().putString("last_used_name", name).apply();

                        callback.sendName(name, points);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("points", points);

        super.onSaveInstanceState(outState);
    }
}
