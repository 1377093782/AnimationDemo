package com.example.liuan.screenview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.liuan.screenview.R;
import com.example.liuan.screenview.view.DoughnutProgress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.view_progress)
    DoughnutProgress viewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.view_progress)
    public void onViewClicked() {
        viewProgress.startRotate();
    }
}
