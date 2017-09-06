package com.young.bezier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.young.bezier.widget.NOrderBezierView;

public class NOrderBezierActivity extends AppCompatActivity implements View.OnClickListener {

    private NOrderBezierView mNOrderBezier;
    private Button mAdd;
    private Button mDel;
    private Button mStart;
    private Button mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_norder_besier);
        initView();
    }

    private void initView() {
        mNOrderBezier = (NOrderBezierView) findViewById(R.id.n_order_bezier);
        mAdd = (Button) findViewById(R.id.add);
        mDel = (Button) findViewById(R.id.del);
        mStart = (Button) findViewById(R.id.start);
        mStop = (Button) findViewById(R.id.stop);

        mAdd.setOnClickListener(this);
        mDel.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                mNOrderBezier.addControlDot();
                break;
            case R.id.del:
                mNOrderBezier.delControlDot();
                break;
            case R.id.start:
                mNOrderBezier.start();
                break;
            case R.id.stop:
                mNOrderBezier.stop();
                break;
        }
    }
}
