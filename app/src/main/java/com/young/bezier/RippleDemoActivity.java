package com.young.bezier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.young.bezier.widget.RippleView;

public class RippleDemoActivity extends AppCompatActivity {

    private RippleView mRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_demo);
        initView();
    }

    private void initView() {
        mRipple = (RippleView) findViewById(R.id.ripple);
        mRipple.startAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRipple.stop();
    }
}
