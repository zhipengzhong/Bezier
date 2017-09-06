package com.young.bezier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBezierDemo;
    private Button mBezierCubicDemo;
    private Button mNOrderBezierDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBezierDemo = (Button) findViewById(R.id.bezier_demo);
        mBezierDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BezierDemoActivity.class));
            }
        });
        mBezierCubicDemo = (Button) findViewById(R.id.bezier_cubic_demo);
        mBezierCubicDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BezierCubicDemoActivity.class));
            }
        });
        mNOrderBezierDemo = (Button) findViewById(R.id.n_order_bezier_demo);
        mNOrderBezierDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NOrderBezierActivity.class));
            }
        });
    }
}
