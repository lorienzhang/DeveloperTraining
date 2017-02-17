package com.baidu.crossfading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;
    private boolean isContentLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentView = findViewById(R.id.content);
        mLoadingView = findViewById(R.id.loading_spinner);

        //隐藏ContentView
        mContentView.setVisibility(GONE);

        //系统提供的短动画的duration
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_crossfade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                isContentLoaded = !isContentLoaded;
                showContentOrLoading(isContentLoaded);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContentOrLoading(boolean isContentLoaded) {
        View showView = isContentLoaded ? mContentView : mLoadingView;
        final View hideView = isContentLoaded ? mLoadingView : mContentView;

//        showView.setVisibility(View.VISIBLE);
//        hideView.setVisibility(View.GONE);

        //设置淡入的view为VISIBLE，并先让其透明
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        //新页面淡入动画
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        //旧页面淡出动画
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
}
