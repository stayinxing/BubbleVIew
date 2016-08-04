package com.example.yinxing.bubbleview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */

public class MainActivity extends AppCompatActivity {

    long mLastTime = 0;
    long mCurTime = 0;
    private final int DELAY = 500;//连续点击的临界点
    BubbleView bubbleView;
    private int mClickCount = 0;
    private int currLikeCount;
    private TextView likeCount;
    private Timer delayTimer;
    private TimerTask timeTask;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            addLiveLick(mClickCount);
            delayTimer.cancel();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bubbleView=(BubbleView)findViewById(R.id.praise_anim);
        bubbleView.setDefaultDrawableList();
        likeCount = (TextView) findViewById(R.id.like_count);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCurTime = System.currentTimeMillis();
                if (mCurTime - mLastTime < DELAY) {
                    mClickCount++;
                } else {
                    mClickCount = 1;
                }
                currLikeCount++;
                likeCount.setText(String.valueOf(currLikeCount));
                delay();
                mLastTime = mCurTime;
                bubbleView.startAnimation(bubbleView.getWidth(), bubbleView.getHeight());
            }
        });
    }

    private void delay() {
        if (timeTask != null)
            timeTask.cancel();

        timeTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                mHandler.sendMessage(message);
            }
        };
        delayTimer = new Timer();
        delayTimer.schedule(timeTask, DELAY);
    }

    /**
     * 点赞请求网络
     */
    private void addLiveLick(int count) {

    }
}
