package com.example.liuan.screenview.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by EdwinWu on 2016/12/8.
 */
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

    private final String TAG = MyDeviceAdminReceiver.class.getSimpleName();

    /**
     * 接收广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 同意
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }


    /**
     * 取消
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }

    /**
     * 用户打开了取消程序设备管理器权限的页面
     */
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Do you really want it disable this security helper?";
    }
}