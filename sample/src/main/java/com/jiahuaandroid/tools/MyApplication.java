package com.jiahuaandroid.tools;

import android.app.Application;

import com.jiahuaandroid.hjhtools.utils.CUtils;

/**
 * Created by jhhuang on 2016/5/13.
 * QQ:781913268
 * 作用：自定义application 用处初始化
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CUtils.init(this);
    }
}
