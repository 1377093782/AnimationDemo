package com.example.animate.utils;

import android.content.Context;
import android.widget.Toast;

import static com.example.animate.common.MainApplication.AppContext;

/**
 * Toast统一管理类
 * 
 */
public class ToastUtils
{

   private ToastUtils()
   {
      /* cannot be instantiated */
      throw new UnsupportedOperationException("cannot be instantiated");
   }

   public static boolean isShow = true;
   private static Toast toast;
   /**
    * 短时间显示Toast
    * 
    * @param context
    * @param message
    */
   public static void showShort(Context context, CharSequence message)
   {
      if (isShow)
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
   }

   /**
    * 短时间显示Toast
    * 
    * @param message
    */
   public static void showShort( CharSequence message)
   {
      if (isShow)
         Toast.makeText(AppContext, message, Toast.LENGTH_SHORT).show();
   }

   /**
    * 长时间显示Toast
    * 
    * @param context
    * @param message
    */
   public static void showLong(Context context, CharSequence message)
   {
      if (isShow)
         Toast.makeText(context, message, Toast.LENGTH_LONG).show();
   }

   public static void showLong( CharSequence message)
   {
      if (isShow)
         Toast.makeText(AppContext, message, Toast.LENGTH_LONG).show();
   }


   /**
    * 自定义显示Toast时间
    * 
    * @param context
    * @param message
    * @param duration
    */
   public static void show(Context context, CharSequence message, int duration)
   {
      if (isShow)
         Toast.makeText(context, message, duration).show();
   }

   /**
    * 自定义显示Toast时间
    * 
    * @param context
    * @param message
    * @param duration
    */
   public static void show(Context context, int message, int duration)
   {
      if (isShow)
         Toast.makeText(context, message, duration).show();
   }


   /**
    * 强大的吐司，能够连续弹的吐司
    * @param text
    */
   public static void showToast(String text){
      if(toast==null){
         //如果等于null，则创建
         toast = Toast.makeText(AppContext, text, Toast.LENGTH_SHORT);
      }else {
         //如果不等于空，则直接将text设置给toast
         toast.setText(text);
      }
      toast.show();
   }

}
