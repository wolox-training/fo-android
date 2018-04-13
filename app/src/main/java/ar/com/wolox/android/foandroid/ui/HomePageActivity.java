package ar.com.wolox.android.foandroid.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.wolmo.core.activity.WolmoActivity;

import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_DEFAULT;
import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_KEY_USER;

import butterknife.BindView;

public class HomePageActivity extends WolmoActivity {

    @BindView(R.id.activity_home_toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.activity_home_tab_layout)
    protected TabLayout mTabLayout;
    @BindView(R.id.activity_home_view_pager)
    protected ViewPager mViewPager;

    @Override
    protected void init() {
        setSupportActionBar(mToolbar);
        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected boolean handleArguments(Bundle args) {
        return getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE).contains(SP_KEY_USER);
    }

    @Override
    protected int layout() {
        return R.layout.activity_home;
    }

    private static class HomePagerAdapter extends FragmentPagerAdapter {

        HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case NEWS_POSITION:
                    return new NewsFragment();
                case PROFILE_POSITION:
                    return new NewsFragment();
                default:
                    throw new RuntimeException("Invalid position " + position + " for HomePagerAdapter#getItem");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private static final int NEWS_POSITION = 0;
    private static final int PROFILE_POSITION = 1;
}
