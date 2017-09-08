package com.giveu.shoppingmall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 图片工具类
 */
public class ImageUtils {

    /**
     * 压缩、旋转 图片文件
     *
     * @param imagePath 文件路径
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeScaleImage(String imagePath, int width, int height) {
        BitmapFactory.Options bitmapOptions = getBitmapOptions(imagePath);
        int scale = calculateInSampleSize(bitmapOptions, width, height);
        LogUtils.i("original wid" + bitmapOptions.outWidth + " original height:" + bitmapOptions.outHeight + " sample:" + scale);
        bitmapOptions.inSampleSize = scale;
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap scaleBitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
        int degree = readPictureDegree(imagePath);
        Bitmap rotateBitmap = null;
        if (scaleBitmap != null && degree != 0) {
            rotateBitmap = rotateBitmap(scaleBitmap, degree);
            scaleBitmap.recycle();
            scaleBitmap = null;
            return rotateBitmap;
        } else {
            return scaleBitmap;
        }
    }

    /**
     * 压缩、旋转 图片文件
     *
     * @param path             文件路径
     * @param isNeedDefaultPic 如果压缩失败，是否需要默认图片
     * @return
     */
    public static Bitmap decodeScaleImage(String path, boolean isNeedDefaultPic) {
        Bitmap bitmap = decodeScaleImage(path, 720, 1280);
        if (bitmap == null && isNeedDefaultPic) {
            try {
                bitmap = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.ic_default_pic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 压缩、旋转 图片文件
     *
     * @param path 文件路径
     * @return
     */
    public static Bitmap decodeScaleImage(String path) {
        return decodeScaleImage(path, true);
    }

    /**
     * 读取图片文件旋转角度
     *
     * @param path 图片的路径
     * @return
     */
    public static int readPictureDegree(String path) {
        short degress = 0;
        try {
            ExifInterface var2 = new ExifInterface(path);
            int var3 = var2.getAttributeInt("Orientation", 1);
            switch (var3) {
                case 3:
                    degress = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    degress = 90;
                    break;
                case 8:
                    degress = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }
        return degress;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 检查照片是否有翻转, 有翻转重新翻转回正常角度
     *
     * @param filePath
     * @return boolean 是否有处理翻转
     */
    public static void checkAndRotatePic(String filePath) {
        Bitmap bitmap = decodeScaleImage(filePath, 720, 1280);
        FileUtils.saveBitmapWithPath(bitmap, filePath);
    }


    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate((float) degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取bitmap属性
     *
     * @param imagePath
     * @return
     */
    public static BitmapFactory.Options getBitmapOptions(String imagePath) {
        BitmapFactory.Options var1 = new BitmapFactory.Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, var1);
        return var1;
    }

    /**
     * 保存图片 通知系统扫描
     *
     * @param activity
     * @param newImageFile
     * @param isInsertToGallary 是否插入系统相册
     * @param isScanFile        是否扫描原来图片
     * @return
     */
    public static String notifysaveImage(Activity activity, File newImageFile, boolean isInsertToGallary, boolean isScanFile) {
        String uri = "";
        if (activity == null || newImageFile == null || newImageFile.length() == 0) {
            return uri;
        }
        try {
            if (isInsertToGallary) {
                uri = MediaStore.Images.Media.insertImage(activity.getContentResolver(), newImageFile.getAbsolutePath(), newImageFile.getName(), "");
            }
            LogUtils.i("uri:" + uri);
            LogUtils.i("ewImageFile.getAbsolutePath():" + newImageFile.getAbsolutePath());
            if (isScanFile) {
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + newImageFile.getAbsolutePath())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 通知系统相册
     *
     * @param activity
     * @param newImageFile
     */
    public static void notifysaveImage(Activity activity, File newImageFile) {
        notifysaveImage(activity, newImageFile, false, true);
    }

    /**
     * 加载
     *
     * @param url
     * @param intoImageView
     * @param options
     * @param listener
     */
    public static void loadImage(String url, ImageView intoImageView, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, intoImageView, options, listener);
    }

    /**
     * 加载网络图片 centerCrop
     *
     * @param url
     * @param placeholderDrawableId
     * @param errorDrawableId
     * @param intoImageView
     */
    public static void loadImage(BitmapDisplayer displayer, String url, int placeholderDrawableId, int errorDrawableId, ImageView intoImageView, ImageLoadingListener listener) {
        if (intoImageView == null) {
            return;
        }

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(placeholderDrawableId)
                .showImageForEmptyUri(placeholderDrawableId)
                .showImageOnFail(errorDrawableId);
        if (displayer != null) {
            builder.displayer(displayer);
        }
        DisplayImageOptions options = builder.build();
        loadImage(url, intoImageView, options, listener);
    }

    public static void loadImage(String url, int placeholderDrawableId, int errorDrawableId, ImageView intoImageView) {
        loadImage(null, url, placeholderDrawableId, errorDrawableId, intoImageView, null);
    }

    public static void loadImageCrop(String url, int placeholderDrawableId, int errorDrawableId, ImageView intoImageView) {
        if (intoImageView != null) {
            intoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        loadImage(url, placeholderDrawableId, errorDrawableId, intoImageView);
    }

    public static void loadImage(String url, int placeholderDrawableId, ImageView intoImageView) {
        loadImage(url, placeholderDrawableId, placeholderDrawableId, intoImageView);
    }

    /**
     * 使用默认占位图
     */
    public static void loadImage(String url, ImageView intoImageView) {
        loadImage(url, R.drawable.ic_default_pic, intoImageView);
    }

    public static void loadImageWithCornerRadius(String url, ImageView intoImageView, int cornerRadius) {
        loadImageWithCorner(url, R.drawable.ic_default_pic, intoImageView, cornerRadius);
    }

    /**
     * 加載圓角圖片
     *
     * @param cornerRadius 圆角弧度
     */
    public static void loadImageWithCorner(String url, int placeholderDrawableId, int errorDrawableId, ImageView intoImageView, int cornerRadius) {
        loadImage(new RoundedBitmapDisplayer(cornerRadius), url, placeholderDrawableId, errorDrawableId, intoImageView, null);
    }

    public static void loadImageWithCorner(String url, int placeholderDrawableId, ImageView intoImageView, int cornerRadius) {
        loadImageWithCorner(url, placeholderDrawableId, placeholderDrawableId, intoImageView, cornerRadius);
    }

    /**
     * 加载本地图片
     */
    public static void loadImage(String imageLoaderType, final String localImagePath, ImageView imageView) {
        loadImage(imageLoaderType + localImagePath, R.drawable.ic_default_pic, imageView);
    }

    public interface ImageLoaderType {
        //    String imageUri = "http://site.com/image.png"; // from Web
        //    String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        //    String imageUri = "content://media/external/audio/albumart/13"; // from content provider
        //    String imageUri = "assets://image.png"; // from assets
        //    String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)

        String file = "file://";
        String content = "content://";
        String assets = "assets://";
        String drawable = "drawable://";

    }
    public interface ImageSize{
        String img_size_240_240 = "/s240x240fdfs/";
    }

    public static Bitmap getViewBitmap(View view, int width, int height) {
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }


    /**
     * 给图片地址添加
     */
    public static String addUrlDomainPrefix(String smallIco) {
        return ApiUrl.BASE_URL + smallIco;
    }

    public static Bitmap drawTimeMask(Context context, String filePath) {
        if (StringUtils.isNull(filePath)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeText = "即有分期 " + format.format(new Date());
        Bitmap bitmap =
                decodeScaleImage(filePath, false);
        FileUtils.saveBitmapWithPath(bitmap, filePath);
//        bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap == null) {
            //该路径下的图片不存在时不往下执行
            return null;
        }
        TextView tv = new TextView(context);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 12);
        TextPaint paint = tv.getPaint();
        paint.setColor(ContextCompat.getColor(context, R.color.color_00adb2));
        // 得到使用该paint写上text的时候,像素为多少
//        float textLength = paint.measureText(timeText);
//        paint.setTextSize(textLength / timeText.length());
//        LogUtils.w("textLength=" + textLength + "");
        Rect bounds = new Rect();
        paint.getTextBounds(timeText, 0, timeText.length(), bounds);
        return drawTextToBitmap(bitmap, timeText, paint,
                bitmap.getWidth() - bounds.width() - DensityUtils.dip2px(10),
                bitmap.getHeight() - DensityUtils.dip2px(10));
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Bitmap bitmap, String text,
                                           Paint paint, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }
}