package hu.bme.androidhazi.moonbuggy;

import com.firebase.client.Firebase;
import com.orm.SugarApp;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public class MoonBuggyApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
