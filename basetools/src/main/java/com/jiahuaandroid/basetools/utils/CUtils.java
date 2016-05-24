package com.jiahuaandroid.basetools.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhhuang on 2016/2/29.
 * QQ: 781913268
 * 作用：项目工具类
 */
public class CUtils {
    private static Application mApplication;
    private static Gson mGson;

    private CUtils() {
    }

    /**
     * 初始化一个Application供全局使用
     *
     * @param application Application 对象
     */
    public static void init(Application application) {
        mApplication = application;
        mGson = new Gson();
    }

    /**
     * @return 返回一个Application对象
     */
    public static Application getApplication() {
        return mApplication;
    }

    /**
     * @return 返回一个Gson对象
     */
    public static Gson getGson() {
        return mGson;
    }

    /**
     * @return 返回当前系统时间
     */
    public static String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * Toast显示数据
     *
     * @param msg
     */
    public static void showMsg(String msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast显示数据
     *
     * @param resId 资源文件id
     */
    public static void showMsg(int resId) {
        String msg = stringFromRes(resId);
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据id从资源文件中获取字符串
     *
     * @param resId
     * @return
     */
    public static String stringFromRes(int resId) {
        String msg = getApplication().getResources().getString(resId);
        return msg;
    }

    /**
     * 获取最顶栈运行的activity的全类名
     *
     * @return
     */
    public static String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }


//    /**
//     * 启动分享界面
//     */
//    public static void showShare() {
//        ShareSDK.initSDK(getApplication());
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(stringFromRes(R.string.share));
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(stringFromRes(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//
//// 启动分享GUI
//        oks.show(getApplication());
//    }

    /**
     * md5加密
     *
     * @param pwd
     * @return
     */
    public static String md5(String pwd) {
        StringBuffer sb = new StringBuffer();
        try {
            //创建用于加密的加密对象
            MessageDigest digest = MessageDigest.getInstance("md5");
            //将字符串转换为一个16位的byte[]
            byte[] bytes = digest.digest(pwd.getBytes("utf-8"));
            for (byte b : bytes) {//遍历
                //与255(0xff)做与运算(&)后得到一个255以内的数值
                int number = b & 255;//也可以& 0xff
                //转化为16进制形式的字符串, 不足2位前面补0
                String numberString = Integer.toHexString(number);
                if (numberString.length() == 1) {
                    numberString = 0 + numberString;
                }
                //连接成密文
                sb.append(numberString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 判断是否联网
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        boolean connect = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connect = networkInfo.isConnected();
        }
        return connect;
    }

    /**
     * 通过uri得到文件地址
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplication().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * 验证手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        String PATTERN = "^1[3|5|7|8][0-9]{9}";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(phone);
        Log.d("phoneMatch", matcher.matches() + "");
        return matcher.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
//            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
//            Network[] allNetworks = connectivityManager.getAllNetworks();
//            if (allNetworks != null && allNetworks.length > 0) {
//                for (int i = 0; i < allNetworks.length; i++) {
//                     NetworkInfo info = connectivityManager.getNetworkInfo(allNetworks[i]);
//                    System.out.println(i + "===状态===" + info.getState());
//                    System.out.println(i + "===类型===" + info.getTypeName());
//                    // 判断当前网络状态是否为连接状态
//                    if (info.getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }

            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }

            }
        }

        return false;
    }

    /**
     * 获取当前屏幕的宽高
     * @return point.x为宽  point.y为高
     */
    public static Point getPoint() {
        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * 获取当前程序的版本号
     * @param context
     * @return
     * @throws Exception
     */

    public static String getVersionName(Context context){
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     * 使用必须添加权限：<uses-permission android:name="android.permission.VIBRATE" />
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
