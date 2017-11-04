package com.example.animate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.animate.R;
import com.example.animate.utils.SpUtils;


public class FormView extends LinearLayout {

    private EditText edit1, edit2;

    public FormView(Context context) {
        super(context);
        loadView();
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView();
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView();
    }

    private void loadView(){
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.form_view, this);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        if(SpUtils.getBoolean("is_login_sucess",false)){
            edit2.setText("haizhe8r");
        }
    }
    public boolean  onPasswordIsTrue(){

        return edit1.getText().toString().trim().equals("HAHA")&&edit2.getText().toString().trim().equals("haizhe8r");
    };
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        edit1.setFocusable(focusable);
        edit2.setFocusable(focusable);
    }
}
