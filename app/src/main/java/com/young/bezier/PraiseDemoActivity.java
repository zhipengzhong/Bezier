package com.young.bezier;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.young.bezier.adapter.PraiseAdapter;

import java.util.ArrayList;
import java.util.Random;

public class PraiseDemoActivity extends AppCompatActivity {

    private static final String TAG = "PraiseDemoActivity";
    private static final float SAMPLING = 1000;
    private RecyclerView mRecycleView;
    private FrameLayout mContainer;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise_demo);
        initView();
        initData();
    }

    private void initData() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Boolean> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            list.add(random.nextBoolean());
        }
        PraiseAdapter adapter = new PraiseAdapter(list);
        adapter.setOnPraiseLinstener(new PraiseAdapter.OnPraiseListener() {
            @Override
            public void onPraise(int x, int y, int width, int height) {
                startPraiseAnim(x, y, width, height);
            }
        });
        mRecycleView.setAdapter(adapter);
        mContainer = (FrameLayout) findViewById(R.id.container);
    }

    private void startPraiseAnim(int x, int y, int width, int height) {
        final ImageView imageView = new ImageView(this);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setImageResource(R.drawable.like_blue);
        mContainer.addView(imageView, width, height);

        Random random = new Random();
        final ArrayList<PointF> pointFs = new ArrayList<>();
        pointFs.add(new PointF(x, y));
        pointFs.add(new PointF(random.nextInt(mScreenWidth), random.nextInt(mScreenHeight)));
        pointFs.add(new PointF(random.nextInt(mScreenWidth), random.nextInt(mScreenHeight)));
        pointFs.add(new PointF(random.nextInt(mScreenWidth), 0));

        ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animValue = (int) animation.getAnimatedValue();
                PointF pointF = buildDotPosition(pointFs, animValue);
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContainer.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private PointF buildDotPosition(ArrayList<PointF> controlDos, float i) {
        ArrayList<ArrayList<PointF>> allDotPosition = new ArrayList<>();
        ArrayList<PointF> rowPosition;
        allDotPosition.add(controlDos);
        int size = controlDos.size();
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

        ArrayList<PointF> pointFs = allDotPosition.get(allDotPosition.size() - 1);
        return pointFs.get(0);
    }
}
