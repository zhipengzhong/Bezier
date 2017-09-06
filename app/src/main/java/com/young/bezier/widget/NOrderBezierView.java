package com.young.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 钟志鹏 on 2017/9/6.
 */

public class NOrderBezierView extends View {

    private static final String TAG = "NOrderBezierView";

    private ArrayList<PointF> mControlDots;
    private Paint mControlPaint;
    private Paint mTextPaint;
    private final float SAMPLING = 1000;
    private Paint mAssistPaint;
    private int[] colors = new int[]{0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFF8B00FF};
    private ArrayList<ArrayList<PointF>> mAllDotPosition = new ArrayList<>();
    private float mCount;
    private boolean mIsDrawAssist = false;
    private Paint mBesierPaint;
    private Path mBesierPath;
    private int mSelectedDot;
    private boolean mStop;

    public NOrderBezierView(Context context) {
        this(context, null);
    }

    public NOrderBezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NOrderBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initPaint();
        mControlDots = new ArrayList<>();
        mControlDots.add(new PointF(100, 100));
        mControlDots.add(new PointF(200, 150));
        mControlDots.add(new PointF(100, 300));

        mBesierPath = new Path();

    }

    private void initPaint() {
        mControlPaint = new Paint();
        mControlPaint.setColor(Color.GRAY);
        mControlPaint.setStrokeWidth(5);
        mControlPaint.setAntiAlias(true);
        mControlPaint.setStyle(Paint.Style.STROKE);

        mAssistPaint = new Paint();
        mAssistPaint.setColor(Color.RED);
        mAssistPaint.setStrokeWidth(5);
        mAssistPaint.setAntiAlias(true);

        mBesierPaint = new Paint();
        mBesierPaint.setColor(Color.BLACK);
        mBesierPaint.setStrokeWidth(5);
        mBesierPaint.setAntiAlias(true);
        mBesierPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(30);
        mTextPaint.setAntiAlias(true);

    }

    private ArrayList<ArrayList<PointF>> buildDotPosition(float i) {
        ArrayList<ArrayList<PointF>> allDotPosition = new ArrayList<>();
        ArrayList<PointF> rowPosition;
        allDotPosition.add(mControlDots);
        int size = mControlDots.size();
        for (int j = 0; j < size; j++) {
            ArrayList<PointF> jPointFs = allDotPosition.get(j);
            rowPosition = new ArrayList<>();
            for (int k = 0; k < jPointFs.size(); k++) {
                if (k != 0) {
                    float x = jPointFs.get(k).x;
                    float y = jPointFs.get(k).y;
                    float x1 = jPointFs.get(k - 1).x;
                    float y1 = jPointFs.get(k - 1).y;
                    float xOffset = Math.abs(x1 - x) * 1.0f * i / SAMPLING;
                    float yOffset = Math.abs(y1 - y) * 1.0f * i / SAMPLING;
                    float x2;
                    float y2;
                    if (x > x1) {
                        x2 = x1 + xOffset;
                    } else {
                        x2 = x1 - xOffset;
                    }
                    if (y > y1) {
                        y2 = y1 + yOffset;
                    } else {
                        y2 = y1 - yOffset;
                    }
                    rowPosition.add(new PointF(x2, y2));
                }
            }
            if (rowPosition.size() != 0) {
                allDotPosition.add(rowPosition);
            }
        }
        return allDotPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCount != 0) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                for (int i = 0; i < mControlDots.size(); i++) {
                    PointF controlDot = mControlDots.get(i);
                    if (Math.abs(controlDot.x - x) <= 50 && Math.abs(controlDot.y - y) <= 50) {
                        mSelectedDot = i;
                        break;
                    } else {
                        mSelectedDot = -1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                float y1 = event.getY();
                if (mSelectedDot >= 0) {
                    mControlDots.get(mSelectedDot).x = x1;
                    mControlDots.get(mSelectedDot).y = y1;
                    mBesierPath.reset();
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
        drawControl(canvas);
        drawAssist(canvas);
        drawBezier(canvas);
    }

    private void drawText(Canvas canvas) {
        String sum = (mControlDots.size() - 1) + "阶贝塞尔曲线";
        Rect rect = measureText(sum);
        canvas.drawText(sum, 0, rect.height(), mTextPaint);
//
        String t = "t= " + mCount;
        Rect rect1 = measureText(t);
        canvas.drawText(t, getMeasuredWidth() - rect1.width(), getMeasuredHeight(), mTextPaint);

        for (int i = 0; i < mControlDots.size(); i++) {
            PointF pointF = mControlDots.get(i);
            String x = String.format("%.2f", pointF.x);
            String y = String.format("%.2f", pointF.y);
            canvas.drawText("P" + i + "(" + x + "," + y + ")", 0, getMeasuredHeight() - ((rect.height() + 10) * i), mTextPaint);
        }
    }

    private Rect measureText(String sum) {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(sum, 0, sum.length(), rect);
        return rect;
    }

    private void drawAssist(Canvas canvas) {
        if (mCount > 0 && mCount < SAMPLING) {
            for (int i = 0; i < mAllDotPosition.size(); i++) {
                if (i == 0) {
                    continue;
                }
                ArrayList<PointF> pointFs = mAllDotPosition.get(i);
                mAssistPaint.setColor(colors[i % colors.length]);
                for (int j = 0; j < pointFs.size(); j++) {
                    float x = pointFs.get(j).x;
                    float y = pointFs.get(j).y;
                    canvas.drawCircle(x, y, 8, mAssistPaint);
                    if (j > 0) {
                        float x1 = pointFs.get(j - 1).x;
                        float y1 = pointFs.get(j - 1).y;
                        canvas.drawLine(x, y, x1, y1, mAssistPaint);
                    }
                }
            }
        }
    }

    //        Path path = new Path();
//        for (int i = 0; i < SAMPLING; i++) {
//            ArrayList<ArrayList<PointF>> allDotPosition = new ArrayList<>();
//            ArrayList<PointF> rowPosition;
//            allDotPosition.add(mControlDots);
//            int size = mControlDots.size();
//            for (int j = 0; j < size; j++) {
//                ArrayList<PointF> jPointFs = allDotPosition.get(j);
//                rowPosition = new ArrayList<>();
//                for (int k = 0; k < jPointFs.size(); k++) {
//                    if (k != 0) {
//                        float x = jPointFs.get(k).x;
//                        float y = jPointFs.get(k).y;
//                        float x1 = jPointFs.get(k - 1).x;
//                        float y1 = jPointFs.get(k - 1).y;
//                        float xOffset = Math.abs(x1 - x) * 1.0f * i / SAMPLING;
//                        float yOffset = Math.abs(y1 - y) * 1.0f * i / SAMPLING;
//                        float x2;
//                        float y2;
//                        if (x > x1) {
//                            x2 = x1 + xOffset;
//                        } else {
//                            x2 = x1 - xOffset;
//                        }
//                        if (y > y1) {
//                            y2 = y1 + yOffset;
//                        } else {
//                            y2 = y1 - yOffset;
//                        }
//                        rowPosition.add(new PointF(x2, y2));
//                    }
//                }
//                if (rowPosition.size() != 0) {
//                    allDotPosition.add(rowPosition);
//                }
//            }
//            ArrayList<PointF> pointFs = allDotPosition.get(allDotPosition.size() - 1);
//            PointF pointF = pointFs.get(pointFs.size() - 1);
//            if (i == 0) {
//                path.moveTo(pointF.x, pointF.y);
//            } else {
//                path.lineTo(pointF.x, pointF.y);
//            }
//            Log.d(TAG, "drawBezier: " + pointF.x + "|" + pointF.y);
//        }
//
//        canvas.drawPath(path, mControlPaint);
    private void drawBezier(Canvas canvas) {
        canvas.drawPath(mBesierPath, mBesierPaint);
    }

    private void drawControl(Canvas canvas) {
        for (int i = 0; i < mControlDots.size(); i++) {
            PointF pointF = mControlDots.get(i);
            canvas.drawCircle(pointF.x, pointF.y, 8, mControlPaint);

            if (i != 0) {
                PointF pointF1 = mControlDots.get(i - 1);
                canvas.drawLine(pointF1.x, pointF1.y, pointF.x, pointF.y, mControlPaint);
            }


            String name = "P" + i;
            Rect rect = new Rect();
            mTextPaint.getTextBounds(name, 0, name.length(), rect);
            canvas.drawText(name, pointF.x, pointF.y, mTextPaint);
        }
    }

    public void addControlDot() {
        if (mCount == 0) {
            Random random = new Random();
            mControlDots.add(new PointF(random.nextInt(getMeasuredWidth()), random.nextInt(getMeasuredHeight())));
            invalidate();
        }
    }

    public void delControlDot() {
        if (mCount == 0) {
            if (mControlDots.size() > 2) {
                mControlDots.remove(mControlDots.size() - 1);
                invalidate();
            }
        }
    }

    public void start() {
        if (mCount == 0) {
            mStop = false;
            startPlay();
        }
    }

    public void stop() {
        mStop = true;
    }

    public void startPlay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mCount <= SAMPLING && !mStop) {
                    mAllDotPosition = buildDotPosition(mCount);
                    ArrayList<PointF> pointFs = mAllDotPosition.get(mAllDotPosition.size() - 1);
                    PointF pointF = pointFs.get(pointFs.size() - 1);
                    if (mCount == 0) {
                        mBesierPath.reset();
                        mBesierPath.moveTo(pointF.x, pointF.y);
                    } else {
                        mBesierPath.lineTo(pointF.x, pointF.y);
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    mCount++;
                    SystemClock.sleep(2);
                }
                mCount = 0;
            }
        }).start();

//        ArrayList<ArrayList<PointF>> arrayLists = buildDotPosition(mCount);
//        ArrayList<PointF> pointFs = arrayLists.get(arrayLists.size() - 1);
//        PointF pointF = pointFs.get(pointFs.size() - 1);
//        if (mCount == 0) {
//            mBesierPath.reset();
//            mBesierPath.moveTo(pointF.x, pointF.y);
//        } else {
//            mBesierPath.lineTo(pointF.x, pointF.y);
//        }
//
//        invalidate();
//        mCount++;
//        if (mCount <= SAMPLING) {
//            SystemClock.sleep(10);
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startPlay();
//                }
//            }, 1);
//        } else {
//            mCount = 0;
//        }
    }
}
