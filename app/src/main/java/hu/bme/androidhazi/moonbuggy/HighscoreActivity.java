package hu.bme.androidhazi.moonbuggy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.androidhazi.moonbuggy.view.GlobalHighscoreListFragment;
import hu.bme.androidhazi.moonbuggy.view.LocalHighscoreListFragment;

public class HighscoreActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.bind(this);

        viewPager.setAdapter(new HighscorePagerAdapter(getSupportFragmentManager()));
    }

    private class HighscorePagerAdapter extends FragmentStatePagerAdapter {

        public HighscorePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LocalHighscoreListFragment();
                case 1:
                    return new GlobalHighscoreListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.localHighscorePageTitle);
                case 1:
                    return getString(R.string.globalHighscorePageTitle);
                default:
                    return null;
            }
        }
    }
}
