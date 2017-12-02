package com.example.liuan.screenview.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.github.promeg.pinyinhelper.Pinyin;
import com.litesuits.common.assist.Check;
import com.litesuits.common.service.NotificationService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by EdwinWu on 2016/11/29.
 */
public class AndroidUtils {

    public static boolean isGrant(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return usageAccessGranted(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean usageAccessGranted(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }


//    //锁的旋转动画，普通动画就行
//    public static void showLockAnimation(final View view, final boolean isLock) {
//        if (view == null) {
//            return;
//        }
//        Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2, view.getHeight() / 2);
//        animation.setDuration(200);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (view instanceof ImageView) {
//                    ((ImageView) view).setImageResource(isLock ? R.drawable.ic_lock : R.drawable.ic_lock_open);
//                    if (isLock) {
//                        ((ImageView) view).setColorFilter(view.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
//                    } else {
//                        ((ImageView) view).setColorFilter(view.getContext().getResources().getColor(R.color.gray_normal_deep), PorterDuff.Mode.SRC_ATOP);
//                    }
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        view.startAnimation(animation);
//    }

    public static boolean isIntentExisting(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }


    public static String getVideoTime(long during) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String dateString = formatter.format(during);
        return dateString;
    }

//    public static Dialog initDialog(final Activity context) {
//        if (context == null) {
//            return null;
//        }
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_last_step);
//        dialog.findViewById(R.id.dlg_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Firebase.getInstance(context).logEvent("访问权限弹窗", "cancel");
//                dialog.dismiss();
//            }
//        });
//        dialog.findViewById(R.id.dlg_ok).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (context instanceof MainActivity) {
//                    ((MainActivity) context).setGoOtherApps(true);
//                    ((MainActivity) context).setShowLockScreen(false);
//                }
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    openPermissionSettingWithOverlay(context, v);
//                } else if (Settings.canDrawOverlays(v.getContext())) {
//                    openPermissionSettingWithOverlay(context, v);
//                } else {
//                    openPermissionSetting(context);
//                }
//                Firebase.getInstance(context).logEvent("访问权限弹窗", "ok");
//                dialog.dismiss();
//                ((AppLockApplication) context.getApplication()).mIsNeedOpenLockScreen = false;
//            }
//
//        });
//        return dialog;
//    }

//    public static Dialog initDialog(final Activity context, final AppsFragment appsFragment) {
//        if (context == null) {
//            return null;
//        }
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_last_step);
//        dialog.findViewById(R.id.dlg_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Firebase.getInstance(context).logEvent("访问权限弹窗", "cancel");
//                dialog.dismiss();
//            }
//        });
//        dialog.findViewById(R.id.dlg_ok).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (context instanceof MainActivity) {
//                    ((MainActivity) context).setGoOtherApps(true);
//                    ((MainActivity) context).setShowLockScreen(false);
//                }
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    openPermissionSettingWithOverlay(appsFragment, v);
//                } else if (Settings.canDrawOverlays(v.getContext())) {
//                    openPermissionSettingWithOverlay(appsFragment, v);
//                } else {
//                    openPermissionSetting(appsFragment);
//                }
//                Firebase.getInstance(context).logEvent("访问权限弹窗", "ok");
//                dialog.dismiss();
//                ((AppLockApplication) context.getApplication()).mIsNeedOpenLockScreen = false;
//            }
//
//        });
//        return dialog;
//    }


//    private static void openPermissionSettingWithOverlay(Activity activity, final View v) {
//        openPermissionSetting(activity);
//        v.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showOverlay(v.getContext().getApplicationContext(), R.layout.accessibility_layout);
//            }
//        }, 1000);
//    }

//    private static void openPermissionSetting(Activity activity) {
//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        //三星和LG的手机有可能没有ACTION_USAGE_ACCESS_SETTINGS
//        try {
//            activity.startActivityForResult(intent, 1002);
//            AppLockApplication.mIsTopActivity = false;
//        } catch (SecurityException e) {
//
//        } catch (ActivityNotFoundException e) {
//
//        }
//    }

//    private static void openPermissionSettingWithOverlay(AppsFragment fragment, final View v) {
//        openPermissionSetting(fragment);
//        v.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showOverlay(v.getContext().getApplicationContext(), R.layout.accessibility_layout);
//            }
//        }, 1000);
//    }

//    private static void openPermissionSetting(AppsFragment fragment) {
//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        //三星和LG的手机有可能没有ACTION_USAGE_ACCESS_SETTINGS
//        try {
//            fragment.startActivityForResult(intent, 1002);
////            System.out.println("AndroidUtils.openPermissionSetting-----fargment");
//            AppLockApplication.mIsTopActivity = false;
//        } catch (SecurityException e) {
//
//        } catch (ActivityNotFoundException e) {
//
//        }
//    }

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

//    //显示透明的引导
//    public static void showOverlay(final Context context, @LayoutRes int layoutId) {
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSPARENT);
//        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        params.gravity = Gravity.CENTER;
//        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View view = inflater.inflate(layoutId, null);
//        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//                ((TextView) v.findViewById(R.id.tv_accessibility_message))
//                        .setText(String.format(context.getString(R.string.accessibility_message), context.getString(R.string.app_name)));
//                v.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        view.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    wm.removeView(view);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                        view.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//                                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                                    try {
//                                        wm.removeView(view);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    return true;
//                                } else {
//                                    return false;
//                                }
//                            }
//                        });
//                    }
//                }, 500);
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//
//            }
//        });
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        view.setSystemUiVisibility(uiOptions);
//        try {
//            wm.addView(view, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//
//    public static void showOverlayActivity(Context context) {
//        Intent intent = new Intent(context, OverlayActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    public static void showOverlayNotificationActivity(Context context) {
//        Intent intent = new Intent(context, OverlayNotificationActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

//    //判断是否开启设备锁
//    public static boolean isDeviceLockEnable(Context context) {
//        // 实例化系统的设备管理器
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        //指定广播接收器
//        ComponentName componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
//        //检测是否已经是设备管理器
//        return devicePolicyManager.isAdminActive(componentName);
//    }

    //获取icon
    public static Drawable getIconFromPackageName(Context context, String packageName) {
        if (Check.isEmpty(packageName)) {
            return null;
        }
        Drawable drawable = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            drawable = packageManager.getApplicationIcon(packageName);
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp.getWidth() > 168 || bmp.getHeight() > 168) {
                    Bitmap bm = Bitmap.createScaledBitmap(bmp, 168, 168, true);
                    return new BitmapDrawable(context.getResources(), bm);
                }
            }
            return drawable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        }
        return drawable;
    }

    public static Bitmap getIconFromPackageNameBitmap(Context context, String packageName) {
        if (Check.isEmpty(packageName)) {
            return null;
        }
        Bitmap bitmap = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            Drawable drawable = packageManager.getApplicationIcon(packageName);
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap.getWidth() > 168 || bitmap.getHeight() > 168) {
                    return Bitmap.createScaledBitmap(bitmap, 168, 168, true);
                }
            }
            return bitmap;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        }
        return bitmap;
    }

    //获取icon
    public static Drawable getIconFromPackageNameSmall(Context context, String packageName) {
        if (Check.isEmpty(packageName)) {
            return null;
        }
        Drawable drawable = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            drawable = packageManager.getApplicationIcon(packageName);
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp.getWidth() > 80 || bmp.getHeight() > 80) {
                    Bitmap bm = Bitmap.createScaledBitmap(bmp, 80, 80, true);
                    return new BitmapDrawable(context.getResources(), bm);
                }
            }
            return drawable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        }
        return drawable;
    }


    //获取label
    public static String getLabelFromPackageName(Context context, String packageName) {
        String label = "";
        if (Check.isEmpty(packageName)) {
            return label;
        }
        PackageManager manager = context.getPackageManager();
        try {
            label = manager.getApplicationLabel(manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }

    //获取时间 2016-12-08 17:43:39
    public static String getTimeyyyyMMddHHmmss(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(time);
        return dateString;
    }

    public static String getTimeyyyyMMdd(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String dateString = format.format(time);
        return dateString;
    }

    public static String getTimeHHmm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }

    public static boolean startGooglePlayByMarketUrl(Context context, String packageName, String zui) {
        if ((packageName == null) || (packageName.length() == 0)) {
            return false;
        }
        String market = "market://details?id=" + packageName + "&referrer=utm_source%3D" + zui;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setPackage("com.android.vending");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(market));
        return startActivity(context, intent);
    }


    public static void startGooglePlayByHttpUrl(Context context, String packageName, String zui) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + packageName + "&referrer=utm_source%3D" + zui));
        context.startActivity(intent);
    }

    public static boolean isInstalled(Context context, String packageName) {
        boolean bInstalled = false;
        if (packageName == null)
            return false;
        PackageInfo packageInfo = null;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();

        }
        if (packageInfo == null) {
            bInstalled = false;
        } else {
            bInstalled = true;
        }
        return bInstalled;
    }

    public static boolean startActivity(Context context, Intent intent) {
        if ((context == null) || (intent == null))
            return false;
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            activityNotFoundException.printStackTrace();
            return false;
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }


    public static String getVersion(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    @SuppressLint("NewApi")
    public static boolean hasUsageAccessModule(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {

            try {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                return list.size() > 0;
            } catch (Exception e) {
                // TODO: handle exception
                return false;
            }
        }
        return false;
    }


    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());

                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 打开权限设置界面
     */
    public static void openSetting(Context context) {
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent1.setData(uri);
            context.startActivity(intent1);
        }
    }


    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

//    public static void startNotificationAccess(final View view, boolean needTip) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            startNotificationActivity(view.getContext());
//            if (needTip) {
//                view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showOverlayNotificationActivity(view.getContext());
//                    }
//                }, 100);
//                view.invalidate();
//            }
//        } else if (Settings.canDrawOverlays(view.getContext().getApplicationContext())) {
//            startNotificationActivity(view.getContext().getApplicationContext());
//            if (needTip) {
//                view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showOverlayNotificationActivity(view.getContext());
//                    }
//                }, 100);
//                view.invalidate();
//            }
//        } else {
//            startNotificationActivity(view.getContext());
//        }
    // }

//    public static void startNotificationAccess(Activity activity, final View view, boolean needTip) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            startNotificationActivity(activity);
//            if (needTip) {
//                view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showOverlayNotificationActivity(view.getContext());
//                    }
//                }, 100);
//                view.invalidate();
//            }
//        } else if (Settings.canDrawOverlays(view.getContext().getApplicationContext())) {
//            startNotificationActivity(activity);
//            if (needTip) {
//                view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showOverlayNotificationActivity(view.getContext());
//                    }
//                }, 100);
//                view.invalidate();
//            }
//        } else {
//            startNotificationActivity(activity);
//        }
//    }

    public static void startNotificationActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (SecurityException e) {

        } catch (ActivityNotFoundException e) {

        }
    }

    public static void startNotificationActivity(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        try {
            activity.startActivityForResult(intent, 105);
        } catch (SecurityException e) {

        } catch (ActivityNotFoundException e) {

        }
    }

    //开启定位服务请求
//    public static void starLocationServer(Context context) {
//        try {
//            Intent intent = new Intent(context, LocationUpdateService.class);
//            intent.putExtra("request", true);
//            context.startService(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取国家码
     */
//    public static String getCountryZipCode(Context context) {
//        String CountryID = "";
//        String CountryZipCode = "";
//        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        CountryID = manager.getSimCountryIso().toUpperCase();
////        System.out.println("CountryID------->" + CountryID);
//        return CountryID;
////        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
////        for (int i = 0; i < rl.length; i++) {
////            String[] g = rl[i].split(",");
////            if (g[1].trim().equals(CountryID.trim())) {
////                CountryZipCode = g[0];
////                break;
////            }
////        }
////        return CountryZipCode;
//    }

    //检测手机是否加了锁屏
    public static boolean checkLockScreenSwitchEnable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return keyguardManager.isKeyguardSecure();
        } else {
            return isSecured(context);
        }
    }

    //判断锁屏是否有密码
    public static boolean checkLockScreenPassword(Context context) {
        String LOCKSCREEN_UTILS = "com.android.internal.widget.LockPatternUtils";
        try {
            Class<?> lockUtilsClass = Class.forName(LOCKSCREEN_UTILS);
            Object lockUtils = lockUtilsClass.getConstructor(Context.class).newInstance(context);
            Method method = lockUtilsClass.getMethod("getActivePasswordQuality");
            Integer mode = (Integer) method.invoke(lockUtils);
            System.out.println("mode=" + mode);

            method = lockUtilsClass.getDeclaredMethod("getBoolean", new Class<?>[]{String.class, boolean.class});
            method.setAccessible(true);
            boolean disabled = (boolean) method.invoke(lockUtils, new Object[]{"lockscreen.disabled", false});
            System.out.println("disabled=" + disabled);

            if (mode == 0) {
                return disabled;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSecured(Context context) {
        boolean isSecured = false;
        String classPath = "com.android.internal.widget.LockPatternUtils";
        try {
            Class<?> lockPatternClass = Class.forName(classPath);
            Object lockPatternObject = lockPatternClass.getConstructor(Context.class).newInstance(context);
            Method method = lockPatternClass.getMethod("isSecure");
            isSecured = (boolean) method.invoke(lockPatternObject);
        } catch (Exception e) {
            isSecured = false;
        }
        return isSecured;
    }

//    //抖动
//    public static Animation mShakeAnimation;
//
//    public static void shakeView(Context context, View view) {
//        if (mShakeAnimation != null) {
//            mShakeAnimation.cancel();
//        }
//        mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.theme_shake);
//        view.startAnimation(mShakeAnimation);
//    }

//    public static void cancelShakeView() {
//        if (mShakeAnimation != null) {
//            mShakeAnimation.cancel();
//        }
//    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static int getScreenheidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    //获取虚拟键的高度
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    public static int parseColor(String color) {

        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            return Color.TRANSPARENT;
        }
    }

    public static boolean needShowAD(Context context) {
        return !(PreferUtils.getIs50PresentUserNoAd(context) &&
                (System.currentTimeMillis() - PreferUtils.getFirstInstall50PresentUserNoAdTime(context) < 24 * 60 * 60 * 1000));
    }

    // 获取可用内存空间大小
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return Math.abs(outInfo.availMem);
    }

    // 获取总运存大小
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i("tatolram:" + str2, num + "\t");
            }
            initial_memory = Long.valueOf(arrayOfString[1]).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (Exception e) {
        }
        return Math.abs(initial_memory);
    }

    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static int getPixColor(Bitmap src) {
        int A, R, G, B;
        int pixelColor;
        int height = src.getHeight();
        int width = src.getWidth();

        int x = width / 2;
        int y = height / 2;
//        System.out.println("src--------------"+Integer.toHexString(src.getPixel(x, y)));
        return src.getPixel(x, y);
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                pixelColor = src.getPixel(x, y);
//                A = Color.alpha(pixelColor);
//                R = Color.red(pixelColor);
//                G = Color.green(pixelColor);
//                B = Color.blue(pixelColor);
//
//                Log.e("A:", A+"");
//                Log.e("R:", R+"");
//                Log.e("G:", G+"");
//                Log.e("B:", B+"");
//
//
//            }
//        }

    }


    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = null;
        try {
            resolveinfo = resolveinfoList.iterator().next();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    //判断是否是字母
    public static boolean checkIsAZaz(String s) {
        if (Check.isEmpty(s)) {
            return false;
        }
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //获取首字母
    public static String getFirstChar(String s) {
        if (s != null && !Check.isEmpty(s.replaceAll("\\s*", ""))) {
            String upperLabel = s.replaceAll("\\s*", "");
            char a = upperLabel.toUpperCase(Locale.US).charAt(0);
            if (Pinyin.isChinese(a)) {
                String ping = Pinyin.toPinyin(a);
                if (!Check.isEmpty(ping)) {
                    if (AndroidUtils.checkIsAZaz(String.valueOf(ping.charAt(0)))) {
                        return String.valueOf(ping.charAt(0));
                    } else {
                        return "#";
                    }
                } else {
                    return "#";
                }
            } else {
                if (AndroidUtils.checkIsAZaz(String.valueOf(a))) {
                    return String.valueOf(a);
                } else {
                    return "#";
                }
            }
        } else {
            return "#";
        }
    }

    public static boolean checkSetPassword(Context context) {
        return !TextUtils.isEmpty(PreferUtils.getNumberPassword(context))
                || !TextUtils.isEmpty(PreferUtils.getPatternPassword(context));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static void setIsEmuiSystem(Context context) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                PreferUtils.setIsDeviceEmui(context, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            PreferUtils.setIsDeviceEmui(context, false);
        }
    }

}
