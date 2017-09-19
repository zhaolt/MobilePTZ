package com.ziguang.ptz.ui.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.base.ViewPagerFragment;
import com.ziguang.ptz.core.data.Media;
import com.ziguang.ptz.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoliangtai on 2017/9/10.
 */

public class AlbumActivity extends FullScreenActivity {

    private TitleBar mTitleBar;

    private ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;

    private SlidingTabLayout mTabLayout;

    private String[] mTabTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mTitleBar = (TitleBar) findViewById(R.id.tool_bar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        mTitleBar.setActionMode(TitleBar.DISPLAY_STYLE_ACTION_ONLY_BACK_IMAGE);
        mTabTitles = getResources().getStringArray(R.array.album_tabs);
        initViewPager();
    }

    private void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {}
        });
        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.textSize4));
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        AlbumFragment bothFragment = new AlbumFragment();
        bothFragment.setContentType(Media.TYPE_BOTH);
        mPagerAdapter.addFragment(bothFragment);
        AlbumFragment photoFragment = new AlbumFragment();
        photoFragment.setContentType(Media.TYPE_IMAGE);
        mPagerAdapter.addFragment(photoFragment);
        AlbumFragment videoFragment = new AlbumFragment();
        videoFragment.setContentType(Media.TYPE_VIDEO);
        mPagerAdapter.addFragment(videoFragment);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, mTabTitles);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<ViewPagerFragment> mFragmentList;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
        }

        public void addFragment(ViewPagerFragment fragment) {
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

    }

}
