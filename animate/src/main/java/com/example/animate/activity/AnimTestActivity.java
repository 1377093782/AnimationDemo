package com.example.animate.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.animate.BaseActivity;
import com.example.animate.R;
import com.example.animate.utils.SpUtils;
import com.example.animate.utils.ToastUtils;
import com.example.animate.view.FormView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class AnimTestActivity extends BaseActivity implements View.OnClickListener {

    public static final String VIDEO_NAME = "welcome_video.mp4";

    private VideoView mVideoView;

    private InputType inputType = InputType.NONE;

    private Button buttonLeft, buttonRight;

    private FormView formView;

    private ViewGroup contianer;

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        setContentView(R.layout.activity_splash_remowe);
        final Button btAnim2 = (Button) findViewById(R.id.bt_anim2);
        final Button btAnim = (Button) findViewById(R.id.bt_anim);
        final TextView tvAnim = (TextView) findViewById(R.id.tv_anim);
        final TranslateAnimation translateAnimation = new TranslateAnimation(-1080, 0, 0, 0);
        translateAnimation.setDuration(3000);

        btAnim2.setAnimation(translateAnimation);


        btAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnim.startAnimation(translateAnimation);
            }
        });
        btAnim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                

                btAnim.startAnimation(translateAnimation);
            }
        });
//        findView();
//
//        initView();
//
//        File videoFile = getFileStreamPath(VIDEO_NAME);
//        if (!videoFile.exists()) {
//            videoFile = copyVideoFile();
//        }
//
//        playVideo(videoFile);
//
//        playAnim();
    }

    private void findView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        contianer = (ViewGroup) findViewById(R.id.container);
        formView = (FormView) findViewById(R.id.formView);
        appName = (TextView) findViewById(R.id.appName);
        formView.post(new Runnable() {
            @Override
            public void run() {
                int delta = formView.getTop() + formView.getHeight();
                formView.setTranslationY(-1 * delta);
            }
        });
    }

    private void initView() {

        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
    }

    private void playVideo(File videoFile) {
        mVideoView.setVideoPath(videoFile.getPath());
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }

    private void playAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0, 1);
        anim.setDuration(4000);
        anim.setRepeatCount(1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appName.setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    private File copyVideoFile() {
        File videoFile;
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
            InputStream in = getResources().openRawResource(R.raw.welcome_video);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
        return videoFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    public void onClick(View view) {
        int delta = formView.getTop() + formView.getHeight();
        switch (inputType) {
            case NONE:

                formView.animate().translationY(0).alpha(1).setDuration(500).start();
                if (view == buttonLeft) {
                    inputType = InputType.LOGIN;
                    buttonLeft.setText(R.string.button_confirm_login);
                    buttonRight.setText(R.string.button_cancel_login);
                } else if (view == buttonRight) {
                    inputType = InputType.SIGN_UP;
                    buttonLeft.setText(R.string.button_confirm_signup);
                    buttonRight.setText(R.string.button_cancel_signup);
                }

                break;
            case LOGIN:

                formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                if (view == buttonLeft) {
                    if (formView.onPasswordIsTrue()) {
                        SpUtils.putBoolean("is_login_sucess", true);
                        startActivity(new Intent(this, ValueAnimActivity.class));
                        finish();
                    } else {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(buttonLeft, "translationX", -20, 20, -10, 10, -5, 5, 0);
                        anim.setDuration(1000);
                        anim.start();
                        ToastUtils.showToast("密码错误,你可能忘了公司的wifi密码！");
                    }
                } else if (view == buttonRight) {

                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
            case SIGN_UP:
                formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                if (view == buttonLeft) {

                } else if (view == buttonRight) {

                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
        }
    }

    enum InputType {
        NONE, LOGIN, SIGN_UP;
    }

}
