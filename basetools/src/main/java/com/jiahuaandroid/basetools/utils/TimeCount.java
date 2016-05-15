package com.jiahuaandroid.basetools.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by jhhuang on 2016/3/24.
 * 作用：验证码倒计时
 */
public class TimeCount extends CountDownTimer{
    private TextView view;
    public TimeCount(TextView view,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.view = view;
    }

    @Override
    public void onFinish() {
        view.setText("重新验证");
        view.setClickable(true);

    }

    @Override
    public void onTick(long millisUntilFinished) {
        view.setClickable(false);
        view.setText(millisUntilFinished / 1000 + "秒");
    }
}
