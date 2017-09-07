package com.young.bezier.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by 钟志鹏 on 2017/9/7.
 */

public class RippleView extends View {

    private static final String TAG = "RippleView";

    private Paint mPaint;
    private Path mPath;
    private float mlevel = 1700;
    private float mRippleNum = 2;
    private ValueAnimator mAnimator;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, getMeasuredHeight());
        float i1 = getMeasuredWidth() / mRippleNum / 2;
        for (int i = 0; i < mRippleNum + 4; i++) {
            if (i == 0) {
                mPath.lineTo(0, mlevel);
                continue;
            }
            if (i % 2 == 0) {
                mPath.quadTo(getMeasuredWidth() * i / mRippleNum - i1, mlevel + (i1 / 8), getMeasuredWidth() * i / mRippleNum, mlevel);
            } else {
                mPath.quadTo(getMeasuredWidth() * i / mRippleNum - i1, mlevel - (i1 / 8), getMeasuredWidth() * i / mRippleNum, mlevel);
            }
        }
        mPath.lineTo(getMeasuredWidth() * (mRippleNum + 2) / mRippleNum, getMeasuredHeight());
        canvas.drawPath(mPath, mPaint);
    }


    public void startAnim() {
        post(new Runnable() {
            @Override
            public void run() {
                mAnimator = ValueAnimator.ofFloat(0, getMeasuredWidth() * 2 / mRippleNum);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.setRepeatMode(ValueAnimator.RESTART);
                mAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mAnimator.setDuration(2000);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float v = (float) animation.getAnimatedValue();
                        scrollTo((int) v, 0);
                        Log.d(TAG, "onAnimationUpdate: " + v);
                    }
                });
                mAnimator.start();
            }
        });
    }

    public void stop() {
        mAnimator.setRepeatCount(0);
    }
}
