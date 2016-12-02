package vn.edu.usth.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.edu.usth.musicplayer.R;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PagerAdapter adapter = new HomeFragmentPagerAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) getView().findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);
    }

    private class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private final int PAGE_COUNT = 3;
        private String titles[];

        public HomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            setupTitles();
        }

        private void setupTitles() {
            String song = getString(R.string.songs);
            String album = getString(R.string.albums);
            String artist = getString(R.string.artists);
            titles = new String[]{song, album, artist};
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int page) {
            switch (page) {
                case 1:
                    return new SongsFragment();
                case 2:
                    return new SongsFragment();
                case 3:
                    return new SongsFragment();
                default:
                    return new SongsFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int page) {
            return titles[page];
        }
    }
}
