package com.giveu.shoppingmall.widget.imageselect;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImageInfo {

    public ImageInfo() {
        super();
        imageFilenameFilter = new ImageFilenameFilter();
    }

    public ImageFilenameFilter imageFilenameFilter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    public List<ImageFloder> imageFloders = new ArrayList<ImageFloder>();// 图片文件夹列表集合
    public List<ImageItem> allImageItem = new ArrayList<ImageItem>();// 所有图片集合

    public static int MAX_SELECT_SIZE = 9;// 本次能选择的最多数量

    /**
     * 选中的图片集合（static）
     */
    public static List<ImageItem> selectImageItems = new ArrayList<ImageItem>();

    /**
     * 图片总数
     */
    private int totalCount;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;

    /**
     * 获取图片
     *
     * @param context
     */
    public void getImage(Context context) {
        String firstImage = null;
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        // 只查询jpeg和png的图片
        String mime_type = MediaStore.Images.Media.MIME_TYPE;
        Cursor mCursor = mContentResolver.query(mImageUri, null, mime_type + "=? or " + mime_type + "=? or " + mime_type + "=?", new String[]{"image/jpeg", "image/png", "image/gif"}, MediaStore.Images.Media.DATE_ADDED + " desc");

        mDirPaths.clear();
        imageFloders.clear();
        allImageItem.clear();
        ImageItem imageItem = null;

        if (mCursor == null) {
            return;
        }
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

            if (StringUtils.isNull(path) || path.contains("%") || path.contains("wbadcache")) {
                continue;
            }

            /**
             * 判断文件是否为空
             */
            File file = new File(path);
            if (file.length() == 0) {
                continue;
            }

            LogUtils.i(path);

            imageItem = new ImageItem(path);
            allImageItem.add(imageItem);

            // 拿到第一张图片的路径
            if (firstImage == null)
                firstImage = path;
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null)
                continue;
            String dirPath = parentFile.getAbsolutePath();
            ImageFloder imageFloder = null;
            // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                // 初始化imageFloder
                imageFloder = new ImageFloder();
                imageFloder.setDir(dirPath);
                imageFloder.setFirstImagePath(path);

                if (parentFile.list(imageFilenameFilter) != null) {
                    int picSize = parentFile.list(imageFilenameFilter).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    imageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
            }
        }
        mCursor.close();
        // 扫描完成，辅助的HashSet也就可以释放内存了
        mDirPaths = null;


        if (!CommonUtils.isNullOrEmpty(allImageItem)) {
            ImageFloder imageAllFloder = new ImageFloder();
            imageAllFloder.setFirstImagePath(allImageItem.get(0).imagePath);
            imageAllFloder.isAllPic = true;
            imageFloders.add(0, imageAllFloder);
        }

    }

    /**
     * 图片选择器
     *
     * @author Administrator
     */
    public static class ImageFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String filename) {
            if (endWith(filename, "jpg") || endWith(filename, "png") || endWith(filename, "jpeg"))
                return true;
            return false;
        }

        private boolean endWith(String filanme, String str) {
            if (filanme.endsWith("." + str.toLowerCase()) || filanme.endsWith("." + str.toUpperCase())) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 重新设置选中状态
     */
    public static void reSetSelect() {
        selectImageItems.clear();
        MAX_SELECT_SIZE = 0;
        imageSize = 0;
    }

    public static int imageSize;

    /**
     * 出去照片还可以选的的数量
     *
     * @return
     */
    public static String getLeftAllSize() {
        int downSize = 0;
        if (selectImageItems.size() > 0) {
            for (ImageItem item : selectImageItems) {
                if (item.isDown) {
                    downSize++;
                }
            }
            imageSize = MAX_SELECT_SIZE - downSize;
            int currentSize = selectImageItems.size() - downSize;
            if (currentSize == 0) {
                return "";
            } else {
                return "完成(" + (currentSize) + ")";
            }
        } else {
            imageSize = MAX_SELECT_SIZE;
            return "";
        }
    }

    public static boolean isImage(String path) {
        File file = new File(path);
        if (file.length() == 0) {
            return false;
        } else {
            return true;
        }
        // try {
        // if (decodeScaleImage(path) == null) {
        // LogUtil.log("null");
        // return false;
        // } else {
        // return true;
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // LogUtil.log("IOException");
        // return false;
        // }
    }

    /**
     * 获取选中图片的路径
     *
     * @return
     */
    public static List<String> getImagePaths() {
        List<String> list = new ArrayList<String>();
        if (!CommonUtils.isNullOrEmpty(selectImageItems)) {
            for (ImageItem item : selectImageItems) {
                if (null != item) {
                    list.add(item.imagePath);
                }
            }
        }
        reSetSelect();
        return list;
    }



}
