package com.jiahuaandroid.basetools.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jhhuang on 2016/3/14.
 */
public class SPUtils {

    private static SharedPreferences sp;
    private static SPUtils instance;

    private SPUtils() {
    }
    public static SPUtils getInstance(Context context) {
        if (instance == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            instance = new SPUtils();
        }
        return instance;
    }

    /**
     * 保存
     */
    public void save(String key, Object value) {
        if (value instanceof String) {
            sp.edit().putString(key, (String)value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }
    }
    /**
     * 读取
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }


    /**
     * 根据key移除
     * @param key
     */
    public void remove(String key) {
        sp.edit().remove(key).commit();
    }

}
