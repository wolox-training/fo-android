package ar.com.wolox.android.foandroid.ui;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Callable;

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

    private PageDescriptor[] mPageDescriptors;

    @Override
    protected void init() {
        setUpPageDescriptors();
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

    private void setUpPageDescriptors() {
        Resources resources = getResources();
        mPageDescriptors = new PageDescriptor[]{
                new PageDescriptor(NewsFragment::new,
                        resources.getDrawable(R.drawable.news_list_icon),
                        R.string.news),
                new PageDescriptor(ProfileFragment::new,
                        resources.getDrawable(R.drawable.profile_icon),
                        R.string.profile)
        };
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.wolox_logo);
        actionBar.setTitle(R.string.wolox);
    }

    private void setUpTabLayout() {
        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mPageDescriptors.length; i++) {
            mTabLayout.getTabAt(i).setCustomView(pagerAdapter.getTabView(i, mTabLayout));
        }
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            try {
                return mPageDescriptors[position].mFragmentFactory.call();
            } catch (Exception e) {
                throw new RuntimeException("Reflective fragment instantiation failed", e);
            }

        }

        @Override
        public int getCount() {
            return mPageDescriptors.length;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return getResources().getString(mPageDescriptors[i].mTitle);
        }

        public View getTabView(int position, ViewGroup parentView) {
            View v = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.tab_icon_and_title, parentView, false);
            ((TextView) v.findViewById(R.id.tab_title)).setText(mPageDescriptors[position].mTitle);
            ((ImageView) v.findViewById(R.id.tab_icon)).setImageDrawable(mPageDescriptors[position].mSelector);
            return v;
        }
    }

    private static class PageDescriptor {
        public Callable<? extends Fragment> mFragmentFactory;
        public Drawable mSelector;
        public int mTitle;

        PageDescriptor(Callable<? extends Fragment> fragmentFactory,
                              Drawable selector,
                              int title) {
            mFragmentFactory = fragmentFactory;
            mSelector = selector;
            mTitle = title;
        }
    }
}
