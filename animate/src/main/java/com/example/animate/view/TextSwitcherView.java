package com.example.animate.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.example.animate.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;  
  
  
/** 
 * Name: TextSwitcherView 
 * Author: liuan 
 * creatTime:2016-12-26 16:46 
 */  
  
public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {  
    private static final int UPDATA_TEXTSWITCHER = 1;  
    private Context mContext;  
    private int index = 0;  
    private ArrayList<String> mReArrayList = new ArrayList<>();  
    private TimerTask timerTask = new TimerTask() {  
        @Override  
        public void run() {  
            Message message = new Message();  
            message.what = UPDATA_TEXTSWITCHER;  
            handler.sendMessage(message);  
        }  
    };  
    private Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            switch (msg.what) {  
                case UPDATA_TEXTSWITCHER:  
                    updateTextSwitcher();  
                    break;  
            }  
        }  
    };  
  
  
    public void getResource(ArrayList<String> reArrayList) {  
        this.mReArrayList = reArrayList;  
    }  
  
    private void updateTextSwitcher() {  
        if (mReArrayList != null && mReArrayList.size() > 0) {  
  
            this.setText(mReArrayList.get(index++));  
            //实现归零  
            if (index > mReArrayList.size() - 1) {  
                index = 0;  
            }  
        }  
    }  
  
    public TextSwitcherView(Context context) {  
        super(context, null);  
    }  
  
    public TextSwitcherView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.mContext = context;  
        init();  
    }  
  
    private void init() {  
        this.setFactory(this);  
        this.setInAnimation(getContext(), R.anim.vcertical_in);
        this.setOutAnimation(getContext(), R.anim.vcertical_out);  
        Timer timer = new Timer();  
        timer.schedule(timerTask, 1, 6000);  
        mReArrayList.add("    我是一名来自百思佳购程序员...其实你们也是哈");
        mReArrayList.add("    点击下面的按钮开始值动画");
        mReArrayList.add("    趁着这个时间，我给大家唱一首《黄昏》吧");
        mReArrayList.add("    依然记得从你口中说出再见坚决如铁....");
        mReArrayList.add("    混乱中有种热泪烧伤的错觉.........");
        mReArrayList.add("    怎么样~~~~~~~~~~~~~~~动听不老铁");
        mReArrayList.add("     长按屏幕会有特效哦");

    }  
  
  
    @Override  
    public View makeView() {  
        //如果想实现跑马灯 可以这么做 不然直接用TextView也是没有关系的  
        //设置自己的TextView样式 继承TextView 重写isFoused方法为true  
        WaterTextView tv = new WaterTextView(getContext());  
/*        Android:ellipsize = "end"　　  省略号在结尾 
        android:ellipsize = "start" 　　省略号在开头 
        android:ellipsize = "middle"     省略号在中间 
        android:ellipsize = "marquee"  跑马灯 
        最好加一个约束android:singleline = "true"*/  
        tv.setSingleLine(true);  
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);  
        //设置重复次数 -1则无线循环  
        tv.setMarqueeRepeatLimit(1);  
        tv.setTextColor(Color.parseColor("#e2658f"));  
        tv.setBackgroundColor(Color.parseColor("#abaaaa"));  
        tv.setTextSize(24);  
        tv.setPadding(20,15,20,15);  
  
        return tv;  
    }  
  
}  
