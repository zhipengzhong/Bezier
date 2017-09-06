package com.young.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 钟志鹏 on 2017/9/5.
 */

public class BezierCubicDemoView extends View {
    private final Paint mPaint;
    private final Paint mDotPaint;
    private final Paint mTextPaint;
    private Path mPath = new Path();
    private PointF mControl1;
    private final PointF mControl2;

    public BezierCubicDemoView(Context context) {
        this(context, null);
    }

    public BezierCubicDemoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCubicDemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint();
        mDotPaint.setColor(Color.RED);
        mDotPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);

        mControl1 = new PointF();
        mControl1.x = 400;
        mControl1.y = 100;
        mControl2 = new PointF();
        mControl2.x = 600;
        mControl2.y = 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if ((Math.abs(x - mControl1.x) + Math.abs(y - mControl1.y)) < (Math.abs(x - mControl2.x) + Math.abs(y - mControl2.y))) {
                    mControl1.x = x;
                    mControl1.y = y;
                } else {
                    mControl2.x = x;
                    mControl2.y = y;
                }

                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(50, 100);
        mPath.cubicTo(mControl1.x, mControl1.y, mControl2.x, mControl2.y, 1000, 100);
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(mControl1.x, mControl1.y, 10, mDotPaint);
        canvas.drawCircle(mControl2.x, mControl2.y, 10, mDotPaint);

        String str1 = "控制点1";
        Rect rect1 = new Rect();
        mTextPaint.getTextBounds(str1, 0, str1.length(), rect1);
        canvas.drawText(str1, mControl1.x - (rect1.width() / 2), mControl1.y + (rect1.height() * 1.5f), mTextPaint);

        String str2 = "控制点2";
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(str2, 0, str2.length(), rect2);
        canvas.drawText(str2, mControl2.x - (rect2.width() / 2), mControl2.y + (rect2.height() * 1.5f), mTextPaint);
    }
}
