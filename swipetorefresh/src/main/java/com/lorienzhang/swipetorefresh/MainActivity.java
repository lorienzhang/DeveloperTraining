package com.lorienzhang.swipetorefresh;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int LIST_ITEMS_COUNT = 20;
    private SwipeRefreshLayout mSwipeRefresh;
    private ListView mListView;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置ListView的数据内容
        mListView = (ListView) findViewById(android.R.id.list);
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Cheeses.randomList(LIST_ITEMS_COUNT));
        mListView.setAdapter(mAdapter);

        //获取滑动刷新控件
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        //设置进度条颜色change_colors
        int color1 = Color.parseColor("#B6DB49");
        int color2 = Color.parseColor("#99CC00");
        int color3 = Color.parseColor("#8ABD00");
        int color4 = Color.parseColor("#7CAF00");
        mSwipeRefresh.setColorSchemeColors(color1, color2, color3, color4);
        //设置滑动更新监听器
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateOperation();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //通过action bar中的action实现刷新数据操作
                mSwipeRefresh.setRefreshing(true);
                //后台任务，更新数据
                updateOperation();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //数据更新完成(后台Task运行结束时调用)，更新UI
    private void onRefreshCompelte(List<String> result) {
        //隐藏SwipeRefreshLayout的进度条
        mSwipeRefresh.setRefreshing(false);

        //更新ListView数据
        mAdapter.clear();
        if (result.size() > 0) {
            for (String cheese : result) {
                mAdapter.add(cheese);
            }
        }

        Toast.makeText(this, "refresh complete", Toast.LENGTH_SHORT).show();
    }

    //开启异步任务，后台更新数据
    private void updateOperation() {
        new DummyUpdateTask().execute();
    }

    private class DummyUpdateTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                //模拟后台更新数据
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return Cheeses.randomList(LIST_ITEMS_COUNT);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            onRefreshCompelte(result);
        }
    }
}
