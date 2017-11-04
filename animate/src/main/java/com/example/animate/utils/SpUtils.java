package com.example.animate.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.animate.common.MainApplication.AppContext;

public class SpUtils {

    private static SharedPreferences sp;

    public static void getSharedPreference(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreference(context);
        sp.edit().putString(key, value).commit();
    }

    public static void putString(String key, String value) {
        getSharedPreference(AppContext);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        getSharedPreference(context);
        return sp.getString(key, defValue);
    }

    public static String getString(String key, String defValue) {
        getSharedPreference(AppContext);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreference(context);
        sp.edit().putInt(key, value).commit();
    }

    public static void putInt(String key, int value) {
        getSharedPreference(AppContext);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        getSharedPreference(context);
        return sp.getInt(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        getSharedPreference(AppContext);
        return sp.getInt(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreference(context);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreference(AppContext);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        getSharedPreference(context);
        return sp.getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key,
                                     boolean defValue) {
        getSharedPreference(AppContext);
        return sp.getBoolean(key, defValue);
    }

    public static void putLong(Context context, String key, Long value) {
        getSharedPreference(context);
        sp.edit().putLong(key, value).commit();
    }

    public static void putLong(String key, Long value) {
        getSharedPreference(AppContext);
        sp.edit().putLong(key, value).commit();
    }

    public static Long getLong(Context context, String key,
                               Long defValue) {
        getSharedPreference(context);
        return sp.getLong(key, defValue);
    }

    public static Long getLong(String key,
                               Long defValue) {
        getSharedPreference(AppContext);
        return sp.getLong(key, defValue);
    }


    /**
     * 移除
     */
    public static void remove(Context context, String key) {
        getSharedPreference(context);
        sp.edit().remove(key).commit();
    }

    public static void remove(String key) {
        getSharedPreference(AppContext);
        sp.edit().remove(key).commit();
    }

} 