package com.baidu.layoutchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    //数组常量，存储一些国家名称
    private static final String[] COUNTRIES = {
            "Belgium", "France", "Italy", "Germany", "Spain",
            "Austria", "Russia", "Poland", "Croatia", "Greece",
            "Ukraine", "China",
    };

    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取ContainerView，布局文件中的LinearLayout
        mContainer = (ViewGroup) findViewById(R.id.container);
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
            case R.id.action_add:

                //如果存在一个item就隐藏提示TextView
                findViewById(android.R.id.empty).setVisibility(GONE);

                addItem();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItem() {

        //压榨item布局
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.list_item,
                mContainer,
                false);

        //设置item中TextView的内容
        ((TextView) newView.findViewById(R.id.country))
                .setText(COUNTRIES[(int) (Math.random() * COUNTRIES.length)]);

        //设置点击item中ImageButton（"X"）的回调
        newView.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //因为ContainerView设置了属性：android:animateLayoutChanges为true
                //当删除或者添加子item时，会有动画效果。
                mContainer.removeView(newView);

                //当没有剩余的item，显示提示TextView
                if (mContainer.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        //将item view添加到container中，如果ContainerView设置了android:animateLayoutChanges为true
        //就会有动画效果
        mContainer.addView(newView, 0);

    }
}
