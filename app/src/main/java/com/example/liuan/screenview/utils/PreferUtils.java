package com.example.liuan.screenview.utils;

import android.content.Context;
import android.provider.Settings;

import com.example.liuan.screenview.ServiceConstant;
import com.litesuits.common.assist.Check;

/**
 * Created by EdwinWu on 2016/11/21.
 */
public class PreferUtils {
    private static String[] mLockAppsArray;
    private static String[] mServiceLockAppArray;

    public interface ABOUT_PASSWORD {
        String DEFAULT_KEYPAD_TYPE = "default_keypad_type";
        String PATTERN_PASSWORD = "gesture_password";
        String NUMBER_PASSWORD = "number_password";
        String CHANGE_PASSWORD = "change_password";
    }

    public interface ABOUT_QA {
        String QUESTION = "question";
        String ANSWER = "answer";
    }

    public interface ABOUT_APP_LOCK {
        String APP_LOCK_PACKAGE = "application_list";
        String SERVICE_VIEW_LOCK_APP = "service_view_lock_app";
        String DEFAULT_SERVICE_VIEW_LOCK_APP_VAULT = "com.whatsapp;com.facebook.katana;com.facebook.orca;com.android;instagram";
        String PUSH_LOCK_SCREEN_THEME = "push_lock_screen_theme";
    }

    public interface ABOUT_SETTING {
        String SHOW_NOTIFICATION_BAR = "show_notification_bar";
        String WITCH_NOTIFICATION_BAR = "witch_notification_bar";
        String UNINSTALL_LOCK_ENABLE = "uninstall_lock";
        String RELOCK_TIME = "relock_time";
        String PATTERN_VISIBLE = "pattern_visible";
        String INTRUDER_SELFIE = "intruder_selfie";
        String VIBRATION = "vibration_feedback";
        String RANDOM = "random_keyboard";
        String THEME = "setting_theme";
        String DISPLAY_HINT = "display_hint";
        String LOCK_NEW_APP = "lock_new_app";
        String DISGUISE_TYPE = "display_type";
        String CHECK_UPDATE = "check_update";
        String VERSION_CODE = "version_code";
        String NEW_PHOTO_VIDEO_ENCRYPTION = "new_photo_video_encryption";
        String SUGGEST_LOCK_NOTIFICATION = "suggest_lock_notification";
        String SUGGEST_LOCK_NOTIFICATION_TIME = "suggest_lock_notification_time";
        String NEW_THEME_PUSH = "new_theme_push";
        String HIDDEN_MESSAGE_FEATURE = "hidden_message_feature";
        String DAILY_PRIVACY_REPORT = "daily_privacy_report";
        String STATIONARY_LOCK_SCREEN = "stationary_lock_screen";
        String WARN_STEAL = "warn_steal";
        String CAMERA_WIDTH = "camera_width";
        String CAMERA_HIGH = "camera_high";
        String LOCK_SCREEN_SWITCH = "lock_screen_switch";
        String QUESTION_ANSWER_TIME = "question_answer_time";
        String FINGERPRINT_TIME = "fingerprint_time";
        String FINGERPRINT_SWITCH = "fingerprint_switch";
        String FINGERPRINT_USE_TIPS = "fingerprint_use_tips";
        String FINGERPRINT_DIALOG_ENABLE = "fingerprint_dialog_enable";
        String FINGERPRINT_DIALOG_LAST_TIME = "fingerprint_dialog_last_time";
        String CHARGING_VIEW_SHOW_SWITCH = "charging_view_show_switch";
        String DAILY_REPORT_LAST_TIME = "daily_report_last_time";
        String FINGER_PRINT_APP_LOCK_OPEN = "finger_print_app_lock_open";
        String FINGER_PRINT_SCREEN_LOCK_OPEN = "finger_print_screen_lock_open";
        String REALLY_FINGER_PRINT_ENABLE = "really_finger_print_enable";
        String AD_DISPLAY_ANIMATOR_TIME = "ad_display_animator_time";
        String PRIVATE_SERVICE_DESK = "private_service_desk";
        String SHOW_NOTIFICATION_PROTECT_DIALOG_TIME = "notification_dialog_time";
        String SHOW_NOTIFICATION_PROTECT_DIALOG_COUNT = "notification_dialog_count";
        String QUICK_FULL_AD_COUNT = "quick_full_ad_count";
        String QUICK_FULL_AD_TIME = "quick_full_ad_time";
        String WARN_STEAL_FIRST = "warm_steal_first";
        String NOTIFICATION_LOCKER_SWITCH = "notification_locker_switch";
        String DEVICE_EMUI = "device_emui";
        String NEWS_ICON_ENABLE = "news_icon_enable";
        String JUNK_FILE_SIZE = "junk_file_size";
        String JUNK_SIZE_LAST_TIME = "junk_size_last_time";
    }

    public interface ABOUT_SERVICE {
        String SERVICE_ENABLE = "service_enable";
    }

    public interface ABOUT_THEME {
        String APPLY_THEME_ID = "apply_theme_id";
        String PIN_THEME_JSON = "pin_theme_json";
        String PATTERN_THEME_JSON = "pattern_theme_json";
        String FIRST_DOWN_LOAD = "first_down_load";
        String LOCK_SCREEN_THEME_JSON = "lock_screen_theme_json";
        String CHANGE_LOCK_SCREEN_THEME_ACTION = ServiceConstant.PACKAGE_NAME + "_change_lock_screen_theme_action";
    }

    public interface ABOUT_BADGE {
        String INTRUDER = "badge_intruder";
        String DISGUISE = "badge_disguise";
        String SETTING = "badge_setting";
        String CHECK_UPDATE = "badge_check_update";
        String RELOCK_TIME = "badge_relock_time";
        String NOTIFICATION_LOCK = "notification_lock";
        String FINGER_PRINT = "badge_finger_print";
    }

    public interface ABOUT_REPORT {
        String NEW_APP_PACKAGES = "new_app_packages";
        String NEW_PHOTO_PATHS = "new_photo_paths";
        String NEW_VIDEO_PATHS = "new_video_paths";
        String NEW_INTRUDER = "new_intruder";
        String FIRST_SHOW_DAILY_SAFE = "first_show_daily_safe";
    }

    public interface ABOUT_AD {
        String FIRST_INSTALL_TIME = "first_install_time";
        String FIRST_INSTALL_TIME_USER_NO_AD_50 = "first_install_time_user_no_ad_50";
        String USER_NO_AD_50 = "user_no_ad_50";
    }

    public interface ABOUT_INSTAGRAM {
        String INSTAGRAM_PIC_HELP_ENABLE = "instagram_pic_help_enable";
        String INSTAGRAM_X = "instagram_x";
        String INSTAGRAM_Y = "instagram_y";
    }

    //设置默认密码模式
    public static void setDefaultKeypadType(Context context, int type) {
        Prefs.with(context).writeInt(ABOUT_PASSWORD.DEFAULT_KEYPAD_TYPE, type);
    }

//    //获取默认密码模式
//    public static int getDefaultKeypadType(Context context) {
//        return Prefs.with(context).readInt(ABOUT_PASSWORD.DEFAULT_KEYPAD_TYPE, PasswordKeypad.GESTURE_KEYPAD);
//    }

    //设置手势密码
    public static void setPatternPassword(Context context, String password) {
        Prefs.with(context).write(ABOUT_PASSWORD.PATTERN_PASSWORD, password);
    }

    //获取手势密码
    public static String getPatternPassword(Context context) {
        return Prefs.with(context).read(ABOUT_PASSWORD.PATTERN_PASSWORD);
    }

    //设置数字密码
    public static void setNumberPassword(Context context, String password) {
        Prefs.with(context).write(ABOUT_PASSWORD.NUMBER_PASSWORD, password);
    }

    //获取数字密码
    public static String getNumberPassword(Context context) {
        return Prefs.with(context).read(ABOUT_PASSWORD.NUMBER_PASSWORD);
    }

    //设置密保问题
    public static void setQuestion(Context context, int question) {
        Prefs.with(context).writeInt(ABOUT_QA.QUESTION, question);
    }

    //获取密保问题
    public static int getQuestion(Context context) {
        return Prefs.with(context).readInt(ABOUT_QA.QUESTION, -1);
    }

    //设置密保答案
    public static void setAnswer(Context context, String answer) {
        Prefs.with(context).write(ABOUT_QA.ANSWER, answer);
    }

    //获取密保答案
    public static String getAnswer(Context context) {
        return Prefs.with(context).read(ABOUT_QA.ANSWER);
    }

    //保存加锁的应用包名
    public static void setLockApp(Context context, String[] items) {
        StringBuilder sb = new StringBuilder();
        for (String s : items) {
            sb.append(s).append(";");
        }
        mLockAppsArray = sb.toString().split(";");
        Prefs.with(context).write(ABOUT_APP_LOCK.APP_LOCK_PACKAGE, sb.toString());
    }

    public static void setServiceLockApp(Context context, String[] items) {
        StringBuilder sb = new StringBuilder();
        for (String s : items) {
            sb.append(s).append(";");
        }
        mServiceLockAppArray = sb.toString().split(";");
        Prefs.with(context).write(ABOUT_APP_LOCK.SERVICE_VIEW_LOCK_APP, sb.toString());
    }

    //保存加锁应用的包名 String
    public static void setLockAppString(Context context, String apps) {
        mLockAppsArray = apps.split(";");
        Prefs.with(context).write(ABOUT_APP_LOCK.APP_LOCK_PACKAGE, apps);
    }

    public static void setServiceLockAppString(Context context, String apps) {
        mServiceLockAppArray = apps.split(";");
        Prefs.with(context).write(ABOUT_APP_LOCK.SERVICE_VIEW_LOCK_APP, apps);
    }

    //获取加锁的应用包名
    public static String getLockApp(Context context) {
        return Prefs.with(context).read(ABOUT_APP_LOCK.APP_LOCK_PACKAGE);
    }

    public static String getServiceLockApp(Context context) {
        return Prefs.with(context).read(ABOUT_APP_LOCK.SERVICE_VIEW_LOCK_APP, ABOUT_APP_LOCK.DEFAULT_SERVICE_VIEW_LOCK_APP_VAULT);
    }

    public static String[] getLockAppStringArray(Context context) {
        if (mLockAppsArray == null) {
            mLockAppsArray = getLockApp(context).split(";");
        }
        return mLockAppsArray;
    }

    public static String[] getServiceLockAppStringArray(Context context) {
        if (mServiceLockAppArray == null) {
            mServiceLockAppArray = getServiceLockApp(context).split(";");
        }
        return mServiceLockAppArray;
    }

    //show notification bar
    public static void setShowNotificationBar(Context context, boolean show) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.SHOW_NOTIFICATION_BAR, show);
    }

    public static boolean getShowNotificationBar(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.SHOW_NOTIFICATION_BAR, false);
    }

    //witch notification
    public static void setWitchNotificationBar(Context context, int id) {
        Prefs.with(context).writeInt(ABOUT_SETTING.WITCH_NOTIFICATION_BAR, id);
    }

    public static int getWitchNotificationBar(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.WITCH_NOTIFICATION_BAR, 0);
    }

    //service enable
    public static void setServiceEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SERVICE.SERVICE_ENABLE, isEnable);
    }

    public static boolean getServiceEnabled(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SERVICE.SERVICE_ENABLE, true);
    }

    //卸载锁保护
    public static void setUninstallLockEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.UNINSTALL_LOCK_ENABLE, isEnable);
    }

    public static boolean getUninstallLockEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.UNINSTALL_LOCK_ENABLE, false);
    }

    //新应用加锁
    public static void setLockNewAppEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.LOCK_NEW_APP, isEnable);
    }

    public static boolean getLockNewAppEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.LOCK_NEW_APP, false);
    }

    //重新加锁时间
    public static void setReLockTime(Context context, int time) {
        Prefs.with(context).writeInt(ABOUT_SETTING.RELOCK_TIME, time);
        setBadgeRelockTimeEnable(context, false);
    }

    public static int getReLockTime(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.RELOCK_TIME, 3);
    }

    //获取当前使用主题的ID
    public static void setApplyThemeId(Context context, String themeId) {
        Prefs.with(context).write(ABOUT_THEME.APPLY_THEME_ID, themeId);
    }

    public static String getApplyThemeId(Context context) {
        return Prefs.with(context).read(ABOUT_THEME.APPLY_THEME_ID);
    }

    //获取PIN JSON 主题
    public static void setPINThemeJson(Context context, String themeJosn) {
        Prefs.with(context).write(ABOUT_THEME.PIN_THEME_JSON, themeJosn);
    }

    public static String getPINThemeJson(Context context) {
        return Prefs.with(context).read(ABOUT_THEME.PIN_THEME_JSON);
    }

    //获取Pattern JSON 主题
    public static void setPatternThemeJson(Context context, String themeJosn) {
        Prefs.with(context).write(ABOUT_THEME.PATTERN_THEME_JSON, themeJosn);
    }

    public static String getPatternThemeJson(Context context) {
        return Prefs.with(context).read(ABOUT_THEME.PATTERN_THEME_JSON);
    }

    //pattern 是否可见
    public static void setPatternVisible(Context context, boolean isVisible) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.PATTERN_VISIBLE, isVisible);
    }

    public static boolean getPatternVisible(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.PATTERN_VISIBLE, true);
    }

    //振动
    public static void setVibrationEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.VIBRATION, isEnable);
    }

    public static boolean getVibrationEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.VIBRATION, false);
    }

    //随机键盘
    public static void setRandomEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.RANDOM, isEnable);
    }

    public static boolean getRandomEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.RANDOM, false);
    }

    //显示锁屏提示
    public static void setDisplayHintEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.DISPLAY_HINT, isEnable);
    }

    public static boolean getDisplayHintEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.DISPLAY_HINT, true);
    }

    //camera 是否打开
    public static void setCameraEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.INTRUDER_SELFIE, isEnable);
        if (isEnable) {
            setBadgeIntruderEnable(context, !isEnable);
        }
    }

    public static boolean getCameraEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.INTRUDER_SELFIE, true);
    }

    //指纹锁类型
//    public static int getDefaultDisguiseType(Context context) {
//        return Prefs.with(context).readInt(ABOUT_SETTING.DISGUISE_TYPE, DisguiseView.NON_DISGUISE_TYPE);
//    }

//    public static void setDefaultDisguiseType(Context context, int type) {
//        Prefs.with(context).writeInt(ABOUT_SETTING.DISGUISE_TYPE, type);
//        if (type != DisguiseView.NON_DISGUISE_TYPE) {
//            setBadgeDisguiseEnable(context, false);
//        }
//    }

    //    设置checkupdata json
//    public static void setCheckUpdateJson(Context context,String json) {
//        Prefs.with(context).write(ABOUT_SETTING.CHECK_UPDATE,json);
//    }
//    public static String getCheckUpdateJson(Context context){
//        return Prefs.with(context).read(ABOUT_SETTING.CHECK_UPDATE);
//    }
    public static int getOldVersion(Context context) {
        return Prefs.with(context).readInt("daily_max_ad", 0);
    }

    public static void setOldVersion(Context context) {
        Prefs.with(context).writeInt("daily_max_ad", 0);
    }

    public static int getVersionCode(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.VERSION_CODE, 0);
    }

    public static void setVersionCode(Context context, int versionCode) {
        Prefs.with(context).writeInt(ABOUT_SETTING.VERSION_CODE, versionCode);
    }

    //入侵者自拍是否显示小红点
    public static boolean getBadgeIntruderEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.INTRUDER, true);
    }

    public static void setBadgeIntruderEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.INTRUDER, isEnable);
    }

    //伪装是否显示小红点
    public static boolean getBadgeDisguiseEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.DISGUISE, true);
    }

    public static void setBadgeDisguiseEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.DISGUISE, isEnable);
    }

    public static boolean getBadgeSettingEnable(Context context) {
        return getBadgeRelockTimeEnable(context) ||
                (getQuestion(context) == -1 || Check.isEmpty(getAnswer(context))) ||
                AndroidUtils.isIntentExisting(context, Settings.ACTION_ACCESSIBILITY_SETTINGS) ? !AndroidUtils.isAccessibilitySettingsOn(context) : false;
    }

    public static boolean getBadgeCheckUpEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.CHECK_UPDATE, true);
    }

    public static void setBadgeCheckUpEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.CHECK_UPDATE, isEnable);
    }

    public static boolean getBadgeRelockTimeEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.RELOCK_TIME, true);
    }

    public static void setBadgeRelockTimeEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.RELOCK_TIME, isEnable);
    }

    public static boolean getBadgeSettingFingerprintEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.FINGER_PRINT, true);
    }

    public static void setBadgeSettingFingerprintEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.FINGER_PRINT, isEnable);
    }


    public static boolean getBadgeNotificationEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_BADGE.NOTIFICATION_LOCK, true);
    }

    public static void setBadgeNotificationLockEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_BADGE.NOTIFICATION_LOCK, isEnable);
    }


    //新图片、视频加密弹窗
    public static boolean getNewPhotoVideoEncryptionEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.NEW_PHOTO_VIDEO_ENCRYPTION, false);
    }

    public static void setNewPhotoVideEncryptionEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.NEW_PHOTO_VIDEO_ENCRYPTION, isEnable);
    }


    //建议加锁弹窗
    public static boolean getSuggestLockEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.SUGGEST_LOCK_NOTIFICATION, true);
    }

    public static void setSuggestLockEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.SUGGEST_LOCK_NOTIFICATION, isEnable);
    }

    //设置建议加锁上次弹窗时间
    public static long getSuggestLockTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_SETTING.SUGGEST_LOCK_NOTIFICATION_TIME, 0);
    }

    public static void setSuggestLockTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_SETTING.SUGGEST_LOCK_NOTIFICATION_TIME, time);
    }

    //设置建议加锁上次弹窗时间
    public static long getSuggestLockTime(Context context, String packageName) {
        return Prefs.with(context).readLong(packageName, 0);
    }

    public static void setSuggestLockTime(Context context, String packageName, long time) {
        Prefs.with(context).writeLong(packageName, time);
    }

    public static long getQuestionAnswerTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_SETTING.QUESTION_ANSWER_TIME, 0);
    }

    public static void setQuestionAnswerTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_SETTING.QUESTION_ANSWER_TIME, time);
    }

    //新主题推送弹窗
    public static boolean getNewThemePushEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.NEW_THEME_PUSH, true);
    }

    public static void setNewThemePushEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.NEW_THEME_PUSH, isEnable);
    }

    //通知信息加锁弹窗
    public static boolean getHiddenMessageEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.HIDDEN_MESSAGE_FEATURE, true);
    }

    public static void SetHiddenMessageEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.HIDDEN_MESSAGE_FEATURE, isEnable);
    }

    //第一次下载主题文件
    public static boolean getFirstDownloadThemeEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_THEME.FIRST_DOWN_LOAD, true);
    }

    public static void setFirstDownloadThemeEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_THEME.FIRST_DOWN_LOAD, isEnable);
    }

    //新安装应用的包名
    public static void setNewAppPackageNames(Context context, String packageNames) {
        Prefs.with(context).write(ABOUT_REPORT.NEW_APP_PACKAGES, packageNames);
    }

    public static String getNewAppPackageNames(Context context) {
        return Prefs.with(context).read(ABOUT_REPORT.NEW_APP_PACKAGES);
    }

    public static String[] getNewAppPackageArray(Context context) {
        String apps = Prefs.with(context).read(ABOUT_REPORT.NEW_APP_PACKAGES);
        if (!Check.isEmpty(apps)) {
            return apps.split(";");
        }
        return null;
    }

    //新图片的路径
    public static void setNewPhotoPaths(Context context, String photoPaths) {
        Prefs.with(context).write(ABOUT_REPORT.NEW_PHOTO_PATHS, photoPaths);
    }

    public static String getNewPhotoPaths(Context context) {
        return Prefs.with(context).read(ABOUT_REPORT.NEW_PHOTO_PATHS);
    }

    public static String[] getNewPhotoPathsArray(Context context) {
        String paths = getNewPhotoPaths(context);
        if (!Check.isEmpty(paths)) {
            return paths.split(";");
        }
        return null;
    }

    //新视频的路径
    public static void setNewVideoPaths(Context context, String photoPaths) {
        Prefs.with(context).write(ABOUT_REPORT.NEW_VIDEO_PATHS, photoPaths);
    }

    public static String getNewVideoPaths(Context context) {
        return Prefs.with(context).read(ABOUT_REPORT.NEW_VIDEO_PATHS);
    }

    public static String[] getNewVideoPathsArray(Context context) {
        String paths = getNewVideoPaths(context);
        if (!Check.isEmpty(paths)) {
            return paths.split(";");
        }
        return null;
    }

    //隐私报告开关
    public static boolean getReportEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.DAILY_PRIVACY_REPORT, true);
    }

    public static void setReportEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.DAILY_PRIVACY_REPORT, isEnable);
    }

    //密码错误1次警告
    public static boolean getWarnStealEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.WARN_STEAL, true);
    }

    public static void setWarnStealEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.WARN_STEAL, isEnable);
    }

    //息屏静止10分钟，弹锁屏
    public static boolean getStationaryLockScreenEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.STATIONARY_LOCK_SCREEN, true);
    }

    public static void setStationaryLockScreenEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.STATIONARY_LOCK_SCREEN, isEnable);
    }

    //保存相机的尺寸
    public static void setCameraWidthSize(Context context, int width) {
        Prefs.with(context).writeInt(ABOUT_SETTING.CAMERA_WIDTH, width);
    }

    public static int getCameraWidthSize(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.CAMERA_WIDTH);
    }

    public static void setCameraHighSize(Context context, int high) {
        Prefs.with(context).writeInt(ABOUT_SETTING.CAMERA_HIGH, high);
    }

    public static int getCameraHighSize(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.CAMERA_HIGH);
    }

    public static void setLockScreenThemeJson(Context context, String themeJosn) {
        Prefs.with(context).write(ABOUT_THEME.LOCK_SCREEN_THEME_JSON, themeJosn);
    }

    public static String getLockScreenThemeJson(Context context) {
        return Prefs.with(context).read(ABOUT_THEME.LOCK_SCREEN_THEME_JSON);
    }

    public static boolean getLockScreenEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.LOCK_SCREEN_SWITCH, true);
    }

    public static void setLockScreenEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.LOCK_SCREEN_SWITCH, isEnable);
    }

    public static long getFirstInstallTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_AD.FIRST_INSTALL_TIME, 0);
    }

    public static void setFirstInstallTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_AD.FIRST_INSTALL_TIME, time);
    }

    public static void setIs50PresentUserNoAD(Context context, boolean isNoAd) {
        Prefs.with(context).writeBoolean(ABOUT_AD.USER_NO_AD_50, isNoAd);
    }

    public static boolean getIs50PresentUserNoAd(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_AD.USER_NO_AD_50, false);
    }

    public static void setFirstInstall50PresentUserNoAdTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_AD.FIRST_INSTALL_TIME_USER_NO_AD_50, time);
    }

    public static long getFirstInstall50PresentUserNoAdTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_AD.FIRST_INSTALL_TIME_USER_NO_AD_50, 0);
    }

    //指纹解锁扫描时间
    public static void setScanFingerprintTime(Context context, int time) {
        Prefs.with(context).writeInt(ABOUT_SETTING.FINGERPRINT_TIME, time);
    }

    public static int getScanFingerprintTime(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.FINGERPRINT_TIME, 1);
    }


    public static boolean getFingerprintEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.FINGERPRINT_SWITCH, false);
    }

    public static void setFingerprintEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.FINGERPRINT_SWITCH, isEnable);
    }

    public static boolean getPrivateServiceDeskEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.PRIVATE_SERVICE_DESK, false);
    }

    public static void setPrivateServiceDeskEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.PRIVATE_SERVICE_DESK, isEnable);
    }

    public static boolean getFingerprintUseTipsEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.FINGERPRINT_USE_TIPS, true);
    }

    public static void setFingerprintUseTipsEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.FINGERPRINT_USE_TIPS, isEnable);
    }



    public static boolean getShowFingerprintDialogEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.FINGERPRINT_DIALOG_ENABLE, true);
    }

    public static void setShowFingerprintDialogEnable(Context context, boolean enable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.FINGERPRINT_DIALOG_ENABLE, enable);
    }


    public static void setLastShowFingerprintDialogTime(Context context, int time) {
        Prefs.with(context).writeInt(ABOUT_SETTING.FINGERPRINT_DIALOG_LAST_TIME, time);
    }

    public static boolean getChargingViewEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.CHARGING_VIEW_SHOW_SWITCH, true);
    }

    public static void setChargingViewEnable(Context context, boolean enable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.CHARGING_VIEW_SHOW_SWITCH, enable);
    }

    public static long getLastShowDailyReportTime(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.DAILY_REPORT_LAST_TIME, 0);
    }

    public static void setLastShowDailyReportTime(Context context, int time) {
        Prefs.with(context).writeInt(ABOUT_SETTING.DAILY_REPORT_LAST_TIME, time);
    }

    public static boolean getLastAppLockFingerOpen(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.FINGER_PRINT_APP_LOCK_OPEN, false);
    }

    public static void setLastAppLockFingerOpen(Context context, boolean isOpen) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.FINGER_PRINT_APP_LOCK_OPEN, isOpen);
    }

    public static boolean getLastScreenLockFingerOpen(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.FINGER_PRINT_SCREEN_LOCK_OPEN, false);
    }

    public static void setLastScreenLockFingerOpen(Context context, boolean isOpen) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.FINGER_PRINT_SCREEN_LOCK_OPEN, isOpen);
    }

    public static boolean getReallyFingerprintEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.REALLY_FINGER_PRINT_ENABLE, false);
    }

    public static void setReallyFingerprintEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.REALLY_FINGER_PRINT_ENABLE, isEnable);
    }

    public static int getLastAdDisplayAnimatorTime(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.AD_DISPLAY_ANIMATOR_TIME, 0);
    }

    public static void setLastAdDisplayAnimatorTime(Context context, int time) {
        Prefs.with(context).writeInt(ABOUT_SETTING.AD_DISPLAY_ANIMATOR_TIME, time);
    }

    public static boolean getFirstShowDailySafeEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_REPORT.FIRST_SHOW_DAILY_SAFE, true);
    }

    public static void setFirstShowDailySafeEnable(Context context, boolean enable) {
        Prefs.with(context).writeBoolean(ABOUT_REPORT.FIRST_SHOW_DAILY_SAFE, enable);
    }


    public static boolean getInstagramHeplEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_INSTAGRAM.INSTAGRAM_PIC_HELP_ENABLE, true);
    }

    public static void setInstagramHeplEnable(Context context, boolean enable) {
        Prefs.with(context).writeBoolean(ABOUT_INSTAGRAM.INSTAGRAM_PIC_HELP_ENABLE, enable);
    }

    public static int getInstagramX(Context context, int defaultX) {
        return Prefs.with(context).readInt(ABOUT_INSTAGRAM.INSTAGRAM_X, defaultX);
    }

    public static void setInstagramX(Context context, int x) {
        Prefs.with(context).writeInt(ABOUT_INSTAGRAM.INSTAGRAM_X, x);
    }

    public static int getInstagramY(Context context, int defaultY) {
        return Prefs.with(context).readInt(ABOUT_INSTAGRAM.INSTAGRAM_Y, defaultY);
    }

    public static void setInstagramY(Context context, int y) {
        Prefs.with(context).writeInt(ABOUT_INSTAGRAM.INSTAGRAM_Y, y);
    }

    public static long getNotificationDialogTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_SETTING.SHOW_NOTIFICATION_PROTECT_DIALOG_TIME);
    }

    public static void setNotificationDialogTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_SETTING.SHOW_NOTIFICATION_PROTECT_DIALOG_TIME, time);
    }

    public static int getNotificationDialogCount(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.SHOW_NOTIFICATION_PROTECT_DIALOG_COUNT);
    }

    public static void setNotificationDialogCount(Context context, int count) {
        Prefs.with(context).writeInt(ABOUT_SETTING.SHOW_NOTIFICATION_PROTECT_DIALOG_COUNT, count);
    }

    public static void setLockScreenPushThemeTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_APP_LOCK.PUSH_LOCK_SCREEN_THEME, time);
    }

    public static long getLockScreenPushThemeTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_APP_LOCK.PUSH_LOCK_SCREEN_THEME);
    }


    public static int getQuickFullAdCount(Context context) {
        return Prefs.with(context).readInt(ABOUT_SETTING.QUICK_FULL_AD_COUNT, 0);
    }

    public static void setQuickFullAdCount(Context context, int count) {
        Prefs.with(context).writeInt(ABOUT_SETTING.QUICK_FULL_AD_COUNT, count);
    }

    public static long getQuickFullAdTime(Context context) {
        return Prefs.with(context).readLong(ABOUT_SETTING.QUICK_FULL_AD_TIME);
    }

    public static void setQuickFullAdTime(Context context, long time) {
        Prefs.with(context).writeLong(ABOUT_SETTING.QUICK_FULL_AD_TIME, time);
    }

    public static void setWarnStealFirstEnable(Context context, boolean enable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.WARN_STEAL_FIRST, enable);
    }

    public static boolean getWarnStealFirstEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.WARN_STEAL_FIRST, true);
    }

    public static void setNotificationLockerSwitchEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.NOTIFICATION_LOCKER_SWITCH, isEnable);
    }

    public static boolean getNotificationLockerSwitchEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.NOTIFICATION_LOCKER_SWITCH, true);
    }

    public static boolean getIsDeviceEmui(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.DEVICE_EMUI, false);
    }

    public static void setIsDeviceEmui(Context context, boolean is) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.DEVICE_EMUI, is);
    }

    public static void setNewsEnable(Context context, boolean isEnable) {
        Prefs.with(context).writeBoolean(ABOUT_SETTING.NEWS_ICON_ENABLE, isEnable);
    }

    public static boolean getNewsEnable(Context context) {
        return Prefs.with(context).readBoolean(ABOUT_SETTING.NEWS_ICON_ENABLE, true);
    }

    public static long getJunkFileSize(Context context) {
        return Prefs.with(context).readLong(ABOUT_SETTING.JUNK_FILE_SIZE, 0);
    }

    public static void setJunkSizeLastTime(Context context, long junkSizeLastTime) {
        Prefs.with(context).writeLong(ABOUT_SETTING.JUNK_SIZE_LAST_TIME, junkSizeLastTime);
    }

    public static void setJunkFileSize(Context context, long cleanSize) {
        Prefs.with(context).writeLong(ABOUT_SETTING.JUNK_FILE_SIZE, cleanSize);
    }

    public static void removeJunkFileSize(Context context) {
        Prefs.with(context).remove(ABOUT_SETTING.JUNK_FILE_SIZE);
    }

    public static void removeJunkTime(Context context) {
        Prefs.with(context).remove(ABOUT_SETTING.JUNK_SIZE_LAST_TIME);
    }

}
