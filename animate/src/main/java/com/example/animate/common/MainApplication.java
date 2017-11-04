package com.example.animate.common;

import android.app.Application;

/**
 * Created by liuan on 2017/11/4.
 */

public class MainApplication extends Application {
    public static MainApplication AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext=this;
    }
}
