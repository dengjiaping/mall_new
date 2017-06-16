package com.giveu.shoppingmall.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by 513419 on 2017/5/31.
 */

public class LubanUtils {

    /**
     * 压缩图片
     *
     * @param filePath
     * @return
     */
    public static File compressPic(@NonNull String filePath) {
        String thumb = filePath;
        File file = new File(filePath);
        double size;
        int angle = getImageSpinAngle(filePath);
        int width = getImageSize(filePath)[0];
        int height = getImageSize(filePath)[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150) return file;

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200) return file;

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return compress(filePath, thumb, thumbW, thumbH, angle, (long) size);
    }

    /**
     * obtain the image rotation angle
     *
     * @param path path of target image
     */
    private static int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * obtain the image's width and height
     *
     * @param imagePath the path of image
     */
    public static int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * 指定参数压缩图片
     * create the thumbnail with the true rotate angle
     *
     * @param largeImagePath the big image path
     * @param thumbFilePath  the thumbnail path
     * @param width          width of thumbnail
     * @param height         height of thumbnail
     * @param angle          rotation angle of thumbnail
     * @param size           the file size of image
     */
    private static File compress(String largeImagePath, String thumbFilePath, int width, int height, int angle, long size) {
        Bitmap thbBitmap = compress(largeImagePath, width, height);

        thbBitmap = rotatingImage(angle, thbBitmap);
        return saveImage(thumbFilePath, thbBitmap, size);
    }

    /**
     * obtain the thumbnail that specify the size
     *
     * @param imagePath the target image path
     * @param width     the width of thumbnail
     * @param height    the height of thumbnail
     * @return {@link Bitmap}
     */
    private static Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 旋转图片
     * rotate the image with specified angle
     *
     * @param angle  the angle will be rotating 旋转的角度
     * @param bitmap target image               目标图片
     */
    private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        //rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        //create a new image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 保存图片到指定路径
     * Save image with specified size
     *
     * @param filePath the image file save path 储存路径
     * @param bitmap   the image what be save   目标图片
     * @param size     the file size of image   期望大小
     */
    public static File saveImage(String filePath, Bitmap bitmap, long size) {
        if (bitmap == null) {
            return null;
        }

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > size && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }
        bitmap.recycle();

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(filePath);
    }

    /**
     * 打水印
     */
    public static void drawSignMask(String photoPath, String nameStr, String addressStr) {
        if (!new File(photoPath).exists()) {
            //该路径下的图片不存在时不往下执行
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap == null) {
            return;
        }
/*        float density = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        LogUtil.e(density + "");
        if (density >= 3) {
            density = density/2;
        }*/
        float rate = (float) (bitmap.getWidth() * 1.0 / DensityUtils.getWidth() > bitmap.getHeight() * 1.0 / DensityUtils.getHeight() ? bitmap.getHeight() * 1.0 / DensityUtils.getHeight() : bitmap.getWidth() * 1.0 / DensityUtils.getWidth());
        TextView tvRight = new TextView(BaseApplication.getInstance());
        tvRight.setTextSize(14 * rate);
        Paint paint = tvRight.getPaint();
        paint.setColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.white));
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        //开始打水印
        if (nameStr == null) {
            nameStr = "";
        }
        Rect nameBounds = new Rect();
        paint.getTextBounds(nameStr, 0, nameStr.length(), nameBounds);


        //右下角地点水印
        if (addressStr == null) {
            addressStr = "";
        }
        Rect addressBounds = new Rect();
        paint.getTextBounds(addressStr, 0, addressStr.length(), addressBounds);
        canvas.drawText(addressStr, bitmap.getWidth() - addressBounds.width() - DensityUtils.dip2px(10),
                bitmap.getHeight() - DensityUtils.dip2px(10), paint);
        //右下角姓名水印
        canvas.drawText(nameStr, bitmap.getWidth() - nameBounds.width() - DensityUtils.dip2px(10),
                bitmap.getHeight() - DensityUtils.dip2px(10) - nameBounds.height(), paint);
        TextView tvTop = new TextView(BaseApplication.getInstance());
        tvTop.setTextSize(24 * rate);
        Paint topPaint = tvTop.getPaint();
        topPaint.setColor(ContextCompat.getColor(BaseApplication.getInstance(), R.color.white));
        //左上角水印
        Calendar calendar = Calendar.getInstance();
        //时间水印
        Rect timeBounds = new Rect();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String timeStr = (hour < 10 ? ("0" + hour) : hour) + ":" + (minute < 10 ? ("0" + minute) : minute);
        topPaint.getTextBounds(timeStr, 0, timeStr.length(), timeBounds);
        canvas.drawText(timeStr, DensityUtils.dip2px(10), DensityUtils.dip2px(30), topPaint);

        //日期水印
        Rect dateBounds = new Rect();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateStr = (year < 10 ? ("0" + year) : year) + "." + (month < 10 ? ("0" + month) : month) + "."
                + (day < 10 ? ("0" + day) : day) + " ";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dateStr += "星期天";
                break;
            case Calendar.MONDAY:
                dateStr += "星期一";
                break;
            case Calendar.TUESDAY:
                dateStr += "星期二";
                break;
            case Calendar.WEDNESDAY:
                dateStr += "星期三";
                break;
            case Calendar.THURSDAY:
                dateStr += "星期四";
                break;
            case Calendar.FRIDAY:
                dateStr += "星期五";
                break;
            case Calendar.SATURDAY:
                dateStr += "星期六";
                break;
        }
        paint.getTextBounds(dateStr, 0, dateStr.length(), dateBounds);
        canvas.drawText(dateStr, DensityUtils.dip2px(10), DensityUtils.dip2px(30) + timeBounds.height(), paint);

        //所属公司水印
        Rect companyBounds = new Rect();
        String companyStr = "即有分期";
        tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.getTextBounds(companyStr, 0, companyStr.length(), companyBounds);
        canvas.drawText(companyStr, DensityUtils.dip2px(10), DensityUtils.dip2px(32) + timeBounds.height() + dateBounds.height(), paint);
        int lineHeight = DensityUtils.dip2px(32) + companyBounds.height()+DensityUtils.dip2px(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(DensityUtils.dip2px(2));
        canvas.drawLine(DensityUtils.dip2px(5), DensityUtils.dip2px(10), DensityUtils.dip2px(5), DensityUtils.dip2px(10) +timeBounds.height()/2+ lineHeight, paint);
        saveImage(photoPath, bitmap, 300);
    }
}
