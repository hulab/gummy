package com.hulab.gummy.example;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.hulab.gummy.listeners.PullClickListener;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnTouchListener(new myPullClickListener(this));
    }

    private class myPullClickListener extends PullClickListener {

        public myPullClickListener(Context context) {
            super(context);
            setResistance(0.9f).setInterpolator(new OvershootInterpolator(4f));
        }

        @Override
        protected void onPull() {
            Toast.makeText(ExampleActivity.this, "Pull", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onClick() {
            Toast.makeText(ExampleActivity.this, "Click", Toast.LENGTH_SHORT).show();
        }
    }
}
