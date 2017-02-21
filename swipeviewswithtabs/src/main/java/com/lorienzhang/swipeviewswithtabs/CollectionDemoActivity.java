package com.lorienzhang.swipeviewswithtabs;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionDemoActivity extends AppCompatActivity {

    private ViewPager mPager;
    private PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);

        //拿到ViewPager
        mPager = (ViewPager) findViewById(R.id.collection_pager);
        mAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        //设置Adapter：FragmentStatePagerAdapter
        mPager.setAdapter(mAdapter);
    }

    //FragmentStatePagerAdapter
    //当Fragments页面太多，并且一开始页面数量并未确定，建议使用这个Adapter；
    //导航的时候，当我们使用这个FragmentStatePagerAdapter，它会自动帮我们销毁Fragment，释放内存；
    public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new CollectionObjectFragment();
            Bundle args = new Bundle();
            args.putInt(CollectionObjectFragment.ARG_COLLECTION_FRAGMENTS,
                    position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT" + (position+1);
        }
    }

    public static class CollectionObjectFragment extends Fragment {
        public static final String ARG_COLLECTION_FRAGMENTS = "COLLECTION_FRAGMENTS";
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_collection_object,
                    container,
                    false);
            Bundle args = getArguments();
            int value = args.getInt(ARG_COLLECTION_FRAGMENTS);
            //设置TextView中的内容
            ((TextView)rootView.findViewById(android.R.id.text1))
                    .setText(value+"");
            return rootView;
        }
    }
}
