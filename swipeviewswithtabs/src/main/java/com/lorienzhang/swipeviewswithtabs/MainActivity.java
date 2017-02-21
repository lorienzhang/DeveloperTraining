package com.lorienzhang.swipeviewswithtabs;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager mViewPager;
    private DemoDummyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //viewpager
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new DemoDummyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        //设置带有Tabs的ActionBar。。。
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //actionbar tabs和viewpager增加联动
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //根据Viewpager的页面，调整actionBar中tabs的位置
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //为Actionbar增加tabs
        for(int i = 0; i < mPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            //为每个Tab设置title
                    .setText(mPagerAdapter.getPageTitle(i))
                            //设置tab的点击监听回调
                    .setTabListener(this)
            );
        }
    }

    //根据对Action Bar中tab的操作，更新Viewpager的页面
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public static class DemoDummyPagerAdapter extends FragmentPagerAdapter {

        public DemoDummyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //不同的页面使用不一样的Fragment，需要判断
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //第一个页面是: LaunchpadFragment
                case 0:
                    return new LaunchpadFragment();
                default:
                    Fragment fragment = new SectionDummyFragment();
                    Bundle args = new Bundle();
                    args.putInt(SectionDummyFragment.ARG_SECTION_NUMBER, position+1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            //三个页面
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "section " + (position+1);
        }
    }

    public static class LaunchpadFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad,
                    container,
                    false);
            //button回调，展示FragmentStatePagerAdapter和strip tile的效果
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO
                            Intent intent = new Intent(getActivity(),
                                    CollectionDemoActivity.class);
                            startActivity(intent);
                        }
                    });
            return rootView;
        }
    }

    public static class SectionDummyFragment extends  Fragment {
        public static final String ARG_SECTION_NUMBER = "section_number";
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy,
                    container,
                    false);

            Bundle args = getArguments();

            //getString()：带参数的字符串资源，R.string.section_dummy
            ((TextView)rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.section_dummy, args.getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }
 }
