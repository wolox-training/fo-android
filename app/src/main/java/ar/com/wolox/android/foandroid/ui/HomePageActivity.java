package ar.com.wolox.android.foandroid.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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

    private final PageDescriptor[] mPageDescriptors = {
            new PageDescriptor(NewsFragment.class, R.drawable.ic_news_list_on, R.drawable.ic_news_list_off),
            new PageDescriptor(ProfileFragment.class, R.drawable.ic_profile_on, R.drawable.ic_profile_off)
    };

    @Override
    protected void init() {
        setSupportActionBar(mToolbar);
        setUpActionBar();
        setUpTabLayout();
    }

    @Override
    protected boolean handleArguments(Bundle args) {
        return getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE).contains(SP_KEY_USER);
    }

    @Override
    protected int layout() {
        return R.layout.activity_home;
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.wolox_logo);
        actionBar.setTitle(R.string.wolox);
    }

    private void setUpTabLayout() {
        mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(mPageDescriptors[0].mSelectedIcon);
        for (int i = 1; i < mPageDescriptors.length; i++) {
            mTabLayout.getTabAt(i).setIcon(mPageDescriptors[i].mUnselectedIcon);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mTabLayout.getTabAt(position).setIcon(mPageDescriptors[position].mSelectedIcon);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mTabLayout.getTabAt(position).setIcon(mPageDescriptors[position].mUnselectedIcon);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            try {
                return mPageDescriptors[position].mFragmentClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Reflective fragment instantiation failed", e);
            }

        }

        @Override
        public int getCount() {
            return mPageDescriptors.length;
        }

    }

    private static class PageDescriptor {
        public Class<? extends Fragment> mFragmentClass;
        public int mSelectedIcon;
        public int mUnselectedIcon;

        PageDescriptor(Class<? extends Fragment> fragmentClass,
                              int selectedIcon,
                              int unselectedIcon) {
            mFragmentClass = fragmentClass;
            mSelectedIcon = selectedIcon;
            mUnselectedIcon = unselectedIcon;
        }
    }

}
