package com.example.animate.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by liuan on 2017/5/16.
 */

public class ToolbarUtils {
    public static void setToolbar(String title, Toolbar mToolBar, final AppCompatActivity activity) {
        mToolBar.setTitle(title);
        activity.setSupportActionBar(mToolBar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });


}

//    public static void setToobarElevation(Toolbar mToolBar) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mToolBar.setElevation(DensityUtil.dip2pxc(5));
//        }
//    }
}
