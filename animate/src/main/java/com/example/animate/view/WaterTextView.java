package com.example.animate.view;
import android.content.Context;  
import android.util.AttributeSet;  
import android.widget.TextView;  
  
/** 
 * Name: WaterTextView 
 * Author: liuan 
 * creatTime:2016-12-26 17:13 
 */  
public class WaterTextView extends TextView {  
    public WaterTextView(Context context) {  
        super(context);  
    }  
  
    public WaterTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
  
  
    public WaterTextView(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
    }  
  
    @Override  
    public boolean isFocused() {  
        return true;  
    }  
}  