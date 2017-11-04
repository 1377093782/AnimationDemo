package com.example.animate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.animate.activity.FrameAnimationActivity;
import com.example.animate.activity.PropertyAnimationActivity;
import com.example.animate.activity.TweenedAnimationActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.bt_am_object_anim)
    Button btAmObjectAnim;
    @Bind(R.id.bt_am_frame_anim)
    Button btAmFrameAnim;
    @Bind(R.id.bt_am_tween_anim)
    Button btAmTweenAnim;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("动画");


    }

    @OnClick({R.id.bt_am_object_anim, R.id.bt_am_frame_anim, R.id.bt_am_tween_anim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_am_object_anim:
                startActivity(new Intent(this, PropertyAnimationActivity.class));
                break;
            case R.id.bt_am_frame_anim:
                startActivity(new Intent(this, FrameAnimationActivity.class));
                break;
            case R.id.bt_am_tween_anim:
                startActivity(new Intent(this, TweenedAnimationActivity.class));
                break;
        }
    }
}
