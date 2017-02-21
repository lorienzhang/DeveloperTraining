package com.lorienzhang.upnavigate;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ActionBar actionBar = getSupportActionBar();
        //显示ActionBar的Up导航
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //当用户点击Up导航，将会执行这里
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                //判断是否需要重新创建新的任务来实现导航
                if(NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    Log.d("TAG", "创建新的返回栈来实现导航");

                    //如果这个activity不是当前应用的一部分，所以，当点击Up导航时，
                    //用合成后退栈（back stack）创建一个新任务
                    TaskStackBuilder.create(this)
                            //添加这个Activity以及父Activity到后退栈中
                            .addNextIntentWithParentStack(upIntent)
                            //向上导航到最近的父Activity
                            .startActivities();
                } else {

                    Log.d("TAG", "直接根据Manifest文件中指定的父Activity进行导航");

                    //这个Activity是应用程序的一部分，所以简单导航到
                    //它的父Activity（在Manifest中指定）即可
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
