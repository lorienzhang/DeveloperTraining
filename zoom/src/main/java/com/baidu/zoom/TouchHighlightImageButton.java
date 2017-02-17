package com.baidu.zoom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by zhanghao43 on 2017/2/15.
 * 自定义ImageButton，改变ImageButton默认的BackGround
 */

public class TouchHighlightImageButton extends ImageButton {

    private static final String TAG = TouchHighlightImageButton.class.getName();

    private Drawable mForegroundDrawable;

    private Rect mCachedBounds = new Rect();

    public TouchHighlightImageButton(Context context) {
        super(context);
        init();
    }

    public TouchHighlightImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchHighlightImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0);
        setPadding(0, 0, 0, 0);

        TypedArray ta = getContext().
                obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
        mForegroundDrawable = ta.getDrawable(0);
        ta.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mForegroundDrawable);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCachedBounds.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //ImageButton每次状态改变会触发重绘
        //将mForegroundDrawable绘制到canvas
        mForegroundDrawable.setBounds(mCachedBounds);
        mForegroundDrawable.draw(canvas);
    }
}
