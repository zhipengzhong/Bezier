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

public class BezierDemoView extends View {
    private Path mPath = new Path();
    private Paint mPaint;
    private Paint mDotPaint;
    private Paint mTextPaint;
    private PointF mPointF;

    public BezierDemoView(Context context) {
        this(context, null);
    }

    public BezierDemoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierDemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        mPointF = new PointF();
        mPointF.x = 500;
        mPointF.y = 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mPointF.x = event.getX();
                mPointF.y = event.getY();
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
        mPath.quadTo(mPointF.x, mPointF.y, 1000, 100);
//        mPath.lineTo(1000, 100);
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(mPointF.x, mPointF.y, 10, mDotPaint);

        String str = "控制点";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, mPointF.x - (rect.width() / 2), mPointF.y + (rect.height() * 1.5f), mTextPaint);
    }
}
