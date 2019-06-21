package com.admin.utill.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Copyright (C)
 *
 * @file: LattePreference
 * @author: 345
 * @Time: 2019/4/21 11:25
 * @description: 提示：
 * Activity.getPreferences(int mode) 生成Activity名.xml 用于Activity内部存储
 * PreferenceManager.getDefaultsharedPreferences(Content) 生成 包名_preferences.xml
 * Context.getSharedPreferences(String name,int mode) 生成name.xml
 */
public class LattePreference {

    /**
     * 每个 应用都有一个默认的配置文件 preferences.xml ，使用getDefaultSharedPreferences 获取
     */
    private static SharedPreferences PREFERENCES;

    private static  String APP_PREFERENCES_KEY ;


    private static final class Holder {
        private static final LattePreference INSTANCE = new LattePreference();
    }

    public static LattePreference getInstance(Context context,String key) {
        PREFERENCES = PreferenceManager.getDefaultSharedPreferences(context);
        APP_PREFERENCES_KEY = key;
        return Holder.INSTANCE;
    }

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    /**
     * @param val 要保存的数据
     */
    public void setAppProfile(String val) {
        getAppPreference()
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    public String getAppProfile() {
        return getAppPreference().getString(APP_PREFERENCES_KEY, null);
    }


    public void removeAppProfile() {
        getAppPreference()
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    public void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    public void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public boolean getAppFlag(String key) {
        return getAppPreference().getBoolean(key, false);
    }

    /**
     * @param key 程序是否是 第一次启动
     * @return 如果使用过则是 true ，否则 为 false
     */
    public boolean booleangetAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    /**
     * sp set 方法
     */
    public void setCustomAppProfile(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    /**
     * sp get 方法
     */
    public String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }
}
