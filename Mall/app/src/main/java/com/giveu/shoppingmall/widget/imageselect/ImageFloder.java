package com.giveu.shoppingmall.widget.imageselect;

import com.giveu.shoppingmall.utils.CommonUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图片文件夹
 *
 * @author Administrator
 */
public class ImageFloder {

    public boolean isAllPic = false;// 是否是全部图片

    public boolean isBuildSubImage = false;// 是否已经得到图片结合
    /**
     * 图片的文件夹路径
     */
    private String dir;

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    public List<ImageItem> imageItmeList;// 文件夹下的图片

    /**
     * 图片的数量
     */
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf + 1);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取文件夹下全部路径
     */
    public void getAllImageItem() {
        File file = new File(dir);
        String[] filePaths = file.list(new ImageInfo.ImageFilenameFilter());
        ImageItem imageItem = null;
        if (filePaths != null && filePaths.length > 0) {
            imageItmeList = new ArrayList<ImageItem>();
            for (int x = 0; x < filePaths.length; x++) {
                String mPath = dir + "/" + filePaths[x];
                /**
                 * 判断文件是否为空
                 */
                File mFile = new File(mPath);
                if (mFile.length() > 0) {
                    imageItem = new ImageItem(mPath);
                    imageItmeList.add(imageItem);
                }
            }
        }
        LogUtils.i("dir:" + dir);
        LogUtils.i("imageItmeList:" + imageItmeList);
        if (!CommonUtils.isNullOrEmpty(imageItmeList)) {
            Collections.reverse(imageItmeList);
        }
        isBuildSubImage = true;
    }
}
