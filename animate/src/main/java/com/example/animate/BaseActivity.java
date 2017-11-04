package com.example.animate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zoomout);
    }
}
