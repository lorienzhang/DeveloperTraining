package com.lorienzhang.upnavigate;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        //首页，不显示ActionBar的Up导航
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    public void next(View view) {
        Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);
    }

    //开启一个外部程序的Activity，以演示Up导航的正确处理方式
    public void external(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }
}
