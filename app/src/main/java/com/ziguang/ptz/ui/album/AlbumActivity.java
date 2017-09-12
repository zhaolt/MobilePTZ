package com.ziguang.ptz.ui.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.base.ViewPagerFragment;
import com.ziguang.ptz.widget.TitleBar;

import java.util.List;

/**
 * Created by zhaoliangtai on 2017/9/10.
 */

public class AlbumActivity extends FullScreenActivity {

    private TitleBar mTitleBar;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mTitleBar = (TitleBar) findViewById(R.id.tool_bar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTitleBar.setActionMode(TitleBar.DISPLAY_STYLE_ACTION_ONLY_BACK_IMAGE);
        initViewPager();
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("照片"));
        mTabLayout.addTab(mTabLayout.newTab().setText("视频"));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<ViewPagerFragment> mFragmentList;

        private List<String> mFragmentTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String title, ViewPagerFragment fragment) {
            mFragmentTitles.add(title);
            mFragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
