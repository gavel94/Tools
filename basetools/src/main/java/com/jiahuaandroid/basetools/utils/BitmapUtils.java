package com.jiahuaandroid.basetools.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jhhuang on 2016/8/4.
 * QQ:781913268
 * Description：BitmapUtils
 */

public class BitmapUtils {
    /**
     * 转换成文件的时候进行的压缩
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static File bitmapCompress2File(Context context, Bitmap bitmap) {
        File file = null;
        try {
            file = FileUtils.createTmpFile(context);
            FileOutputStream out = new FileOutputStream(file);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            LogUtil.d("TAG", "compress:" + compress);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    // 从sd卡上加载图片
    public static Bitmap decodeSampledBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) {   // 如果没有缩放，那么不回收
            src.recycle();  // 释放Bitmap的native像素数组
        }
        return dst;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Bitmap对象转换为二进制数据
     *
     * @param srcBmp
     *            Bitmap对象
     * @return 二进制数据
     */
    public static byte[] convertBitmapToBytes(Bitmap srcBmp) {
        if (isBitmapAvailable(srcBmp)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            srcBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    /**
     * 判断Bitmap对象是否有效
     *
     * @param bmp
     *            Bitmap对象
     * @return true if bitmap is not null and not be recycled
     */
    public static boolean isBitmapAvailable(Bitmap bmp) {
        return bmp != null && !bmp.isRecycled();
    }

    /**
     * 二进制数值转换为Bitmap对象
     *
     * @param srcBytes
     *            二进制数据
     * @return Bitmap对象, 使用完后注意回收
     */
    public static Bitmap convertBytesToBitmap(byte[] srcBytes) {
        if (srcBytes != null && srcBytes.length > 0) {
            return BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length);
        }
        return null;
    }

    /**
     * 缩放Bitmap对象
     *
     * @param srcBmp
     *            Bitmap对象
     * @param width
     *            缩放后的宽度
     * @param height
     *            缩放后的高度
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap resizeBitmap(Bitmap srcBmp, int width, int height) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int w = srcBmp.getWidth();
            int h = srcBmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) width / w), ((float) height / h));
            dstBmp = Bitmap.createBitmap(srcBmp, 0, 0, w, h, matrix, true);
        }
        return dstBmp;
    }

    /**
     * 读取Bitmap文件，并作缩放处理
     *
     *            Bitmap对象
     * @param width
     *            缩放后的宽度
     * @param height
     *            缩放后的高度
     * @return 缩放后的Bitmap对象, 使用完后注意回收
     */
    public static Bitmap getScaledBitmapFromFile(String filePath, int width, int height) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inPurgeable = true;
        op.inInputShareable = true;
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, op);

        op.inSampleSize = (int) Math.max(((float) op.outWidth / width), ((float) op.outHeight / height));
        op.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, op);
    }

    /**
     * Bitmap对象转换为Drawable对象
     *
     * @param bmp
     *            Bitmap对象
     * @return Drawable对象
     */
    @SuppressWarnings("deprecation")
    public static Drawable convertBitmapToDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    /**
     * 无拉伸图压缩，并截取中间部分
     * @param source 原图
     * @param newHeight 缩略图高度
     * @param newWidth	缩略图宽度
     * @return 缩略图
     */
    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        if(source==null || source.isRecycled()){
            throw new IllegalArgumentException("source bitmap for scale is not available");
        }

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    /**
     * Drawable对象转换为Bitmap对象
     *
     * @param drawable
     *            Drawable对象
     * @return Bitmap对象, 使用完后注意回收
     */
    public static Bitmap convertDrawbaleToBitmap(Drawable drawable) {
        Bitmap dstBmp = null;
        if (drawable != null) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Bitmap.Config config =
                    (drawable.getOpacity() != PixelFormat.OPAQUE) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            dstBmp = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(dstBmp);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        }
        return dstBmp;
    }

    /**
     * 获取带圆角的图片
     *
     * @param srcBmp
     *            原图
     * @param round
     *            圆角半径
     * @param roundColor
     *            圆角填充颜色
     * @return 带圆角的Bitmap图片
     */
    public static Bitmap createRoundCornerBitmap(Bitmap srcBmp, float round, int roundColor) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();

            dstBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Rect rect = new Rect(0, 0, width, height);
            Canvas canvas = new Canvas(dstBmp);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(roundColor);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(new RectF(rect), round, round, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(srcBmp, rect, rect, paint);
        }
        return dstBmp;
    }

    /**
     * 获取带倒影的图片
     *
     * @param srcBmp
     *            原图
     * @param gap
     *            原图与倒影间的空隙
     * @return 带倒影效果的Bitmap图片
     */
    public static Bitmap createReflectionBitmap(Bitmap srcBmp, int gap) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();
            int tmpGap = Math.min(gap, height / 2);

            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);

            Bitmap tmpBmp = Bitmap.createBitmap(srcBmp, 0, height / 2, width, height / 2, matrix, false);
            dstBmp = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(dstBmp);
            Paint paint = new Paint();
            canvas.drawBitmap(srcBmp, 0, 0, null);
            canvas.drawRect(0, height, width, height + tmpGap, paint);
            canvas.drawBitmap(tmpBmp, 0, height + tmpGap, null);

            LinearGradient shader = new LinearGradient(0, srcBmp.getHeight(),
                    0, dstBmp.getHeight() + tmpGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawRect(0, height, width, dstBmp.getHeight() + tmpGap, paint);

            if (tmpBmp != null) {
                tmpBmp.recycle();
                tmpBmp = null;
            }
        }
        return dstBmp;
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 75, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过decodeStream方式获取资源图片
     * @param context 上下文对象
     * @param resId  资源id
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        //获取资源
        InputStream inputStream = context.getResources().openRawResource(resId);
        Bitmap bitmap = null;
        try {
//            bitmap = BitmapFactory.decodeStream(inputStream,null,options) ;
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (OutOfMemoryError e) {
            LogUtil.e("BitmapUtils","decode bitmap error");
        }
        if(bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        }
        return bitmap;
    }

    /**
     * 调整图片的大小 (先将图片缩小或者放大，然后对图片进行裁剪，让不同的图片都能占满不同的手机尺寸的屏幕)
     * @param boxWidth
     * @param boxHeight
     * @param bitmap 原图
     * @return
     */
    public static Bitmap resizeBitmap(int boxWidth, int boxHeight, Bitmap bitmap) {

        float scaleX = ((float) boxWidth) / ((float) bitmap.getWidth());
        float scaleY = ((float) boxHeight) / ((float) bitmap.getHeight());
        float scale = 1.0f;

        if ((scaleX >= scaleY && scaleY >= 1.0f) || (scaleX > scaleY && scaleX < 1.0f) || (scaleX >= 1.0f && scaleY < 1.0f)) {
            scale = scaleX;
        }
        if ((scaleY > scaleX && scaleX >= 1.0f) || (scaleY > scaleX && scaleY < 1.0f) || (scaleX < 1.0f && scaleY >= 1.0f)) {
            scale = scaleY;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap alterBitmap = Bitmap.createBitmap(newBitmap, 0, 0, boxWidth, boxHeight);
        newBitmap = null;
        return alterBitmap;
    }
}
