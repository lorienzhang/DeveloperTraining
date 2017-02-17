package com.baidu.screenslide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final int NUM_PAGES = 5;

    private ViewPager mPager;

    private PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                //page页面切换就重置AnctionBar；因为action bar的状态依赖于当前活动的page页面
                //还有一种解决方案，让每个fragment自己暴露Action Bar
                //这里为了简单，使用Activity统一暴露Action Bar
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        //第一页禁用previous按钮，最后一页禁用next按钮
        menu.findItem(R.id.action_previous).setEnabled(
                mPager.getCurrentItem() > 0);
        menu.findItem(R.id.action_next).setEnabled(
                mPager.getCurrentItem() < mAdapter.getCount() - 1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;
            case R.id.action_next:
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ScreenSlidePageAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
