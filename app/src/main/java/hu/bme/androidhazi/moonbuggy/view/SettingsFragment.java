package hu.bme.androidhazi.moonbuggy.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hu.bme.androidhazi.moonbuggy.R;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.xmlsettings);
    }
}
