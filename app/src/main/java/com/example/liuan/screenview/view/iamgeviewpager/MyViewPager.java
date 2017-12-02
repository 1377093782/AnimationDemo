package com.example.liuan.screenview.view.iamgeviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 吴建利 on 2016/9/3.
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context){
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        try{
            return super.onInterceptTouchEvent(ev);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }
}
