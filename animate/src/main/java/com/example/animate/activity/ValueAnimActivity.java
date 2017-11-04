package com.example.animate.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.animate.BaseActivity;
import com.example.animate.MainActivity;
import com.example.animate.R;
import com.example.animate.view.TextSwitcherView;
import com.zys.brokenview.BrokenCallback;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ValueAnimActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.ll_ava)
    LinearLayout llAva;
    @Bind(R.id.tv_ava)
    TextSwitcherView tvAva;
    @Bind(R.id.btn2)
    Button btn2;
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_anim);
        ButterKnife.bind(this);
        toolbar.setTitle("值动画");
        btn1 = (Button) findViewById(R.id.btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setVisibility(View.VISIBLE);
                countDown();
                //可以放发送验证码的逻辑
            }


        });
    }


    private void countDown() {

        btn1.setEnabled(false);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(30, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int valeu = (int) valueAnimator.getAnimatedValue();
                btn1.setText(valeu + "");

            }


        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                btn1.setEnabled(true);
//                btn1.setText("点击倒计时");
                showOver();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());//匀速
        valueAnimator.setDuration(30 * 1000);
        valueAnimator.start();

    }

    private void showOver() {
        btn1.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        tvAva.setVisibility(View.GONE);
        BrokenView brokenView = BrokenView.add2Window(ValueAnimActivity.this);
        BrokenTouchListener buildListener = new BrokenTouchListener.Builder(brokenView).
                build();
        btn2.setVisibility(View.GONE);
        llAva.setOnTouchListener(buildListener);
        llAva.setBackgroundResource(R.mipmap.doutu_1);
        brokenView.setCallback(new MyCallBack());
    }

    @OnClick(R.id.btn2)
    public void onViewClicked() {
        btn1.setEnabled(true);
        btn2.setVisibility(View.GONE);
        showOver();
    }

    private static final String TAG = "ValueAnimActivity";
    private class MyCallBack extends BrokenCallback {
//        @Override
//        public void onStart(View v) {
//            showCallback(v,"onStart");
//        }

        @Override
        public void onCancel(View v) {
            showCallback(v,"onCancel");
            Log.e(TAG, "onCancel: ");
        }

//        @Override
//        public void onRestart(View v) {
//            showCallback(v,"onRestart");
//        }

//        @Override
//        public void onFalling(View v) {
//            showCallback(v,"onFalling");
//            Log.e(TAG, "onFalling: " );
//        }

        @Override
        public void onFallingEnd(View v) {
            showCallback(v,"onFallingEnd");
            Log.e(TAG, "onFallingEnd: " );
        }

        @Override
        public void onCancelEnd(View v) {
            showCallback(v, "onCancelEnd");
            Log.e(TAG, "onCancelEnd: " );
        }
    }

    public void showCallback(View v, String s) {
        switch (v.getId()) {
            case R.id.ll_ava:
                Snackbar.make(llAva, "啦啦啦 下个页面走起" /*+ s*/, Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ValueAnimActivity.this, MainActivity.class));
                        finish();
                    }
                }, Snackbar.LENGTH_SHORT);
                break;

        }
    }
}
