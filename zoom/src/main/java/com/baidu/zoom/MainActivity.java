package com.baidu.zoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private int mShorAnimDuration;

    private Animator mCurrentAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取系统默认的短动画时长
        mShorAnimDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        final View thumb1view = findViewById(R.id.thumb_button_1);
        final View thumb2view = findViewById(R.id.thumb_button_2);
        thumb1view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb1view, R.drawable.image1);
            }
        });
        thumb2view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb2view, R.drawable.image2);
            }
        });
    }


    /**
     * 放大缩略图
     * 首先隐藏大图，如果用户点击了thumbnail，就隐藏thumbnail；然后显示大图，
     * 并对大图做animation（X,Y,SCALE_X,SCALE_Y），这样给用户的感觉就是从thumbnial缩放到大图。
     * 代码的关键就是动画处理：坐标的计算---math
     * @param thumbView
     * @param imageResId
     */
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        if(mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);

        //绝对坐标转化成相对坐标
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        //使用center crop技术对图片进行裁剪，防止在动画过程中产生不必要的拉伸
        //目的就是，保持thumbnial的纵横比和大图的纵横比一致
        float startScale;
        if( (float) finalBounds.width() / finalBounds.height() >
                (float) startBounds.width() / startBounds.height()) {
            //对startBounds进行水平拉伸
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = finalBounds.width() * startScale;
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            //对startBounds进行垂直拉伸
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = finalBounds.height() * startScale;
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        //隐藏缩略图，显示大图
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        //设置缩放中心点为：left，top
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
        //坐标数据计算完毕，开始执行动画
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView,
                        View.X,
                        startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.Y,
                        startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y,
                        startScale,
                        1f));
        set.setDuration(mShorAnimDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        //点击大图，大图缩小
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView,
                                View.X,
                                startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.Y,
                                startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.SCALE_X,
                                startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.SCALE_Y,
                                startScaleFinal));
                set.setDuration(mShorAnimDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

}
