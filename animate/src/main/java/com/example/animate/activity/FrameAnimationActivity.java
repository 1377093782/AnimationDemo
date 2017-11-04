package com.example.animate.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.animate.BaseActivity;
import com.example.animate.R;
import com.example.animate.utils.ToolbarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FrameAnimationActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.animation1)
    ImageView animation1;
    @Bind(R.id.animation2)
    ImageView animation2;
    @Bind(R.id.animation3)
    ImageView animation3;
    @Bind(R.id.animation4)
    ImageView animation4;
    private ImageView animationImg1, animationImg2, animationImg3, animationImg4;
    //
    private AnimationDrawable animationDrawable1, animationDrawable2, animationDrawable3, animationDrawable4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        ButterKnife.bind(this);
        initView();
        ToolbarUtils.setToolbar("帧动画",toolbar,this);
    }


    public void initView() {
        animationImg1 = (ImageView) findViewById(R.id.animation1);
        animationImg1.setImageResource(R.drawable.frame_anim1);
        animationDrawable1 = (AnimationDrawable) animationImg1.getDrawable();
        animationDrawable1.start();

        animationImg2 = (ImageView) findViewById(R.id.animation2);
        animationImg2.setImageResource(R.drawable.frame_anim2);
        animationDrawable2 = (AnimationDrawable) animationImg2.getDrawable();
        animationDrawable2.start();

        animationImg3 = (ImageView) findViewById(R.id.animation3);
        animationImg3.setImageResource(R.drawable.frame_anim3);
        animationDrawable3 = (AnimationDrawable) animationImg3.getDrawable();
        animationDrawable3.start();

        animationImg4 = (ImageView) findViewById(R.id.animation4);
        animationImg4.setImageResource(R.drawable.frame_anim4);
        animationDrawable4 = (AnimationDrawable) animationImg4.getDrawable();
        animationDrawable4.start();
    }

    @Override
    public void finish() {
        super.finish();

    }
}
