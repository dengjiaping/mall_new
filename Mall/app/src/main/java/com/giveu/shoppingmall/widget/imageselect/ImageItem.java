package com.giveu.shoppingmall.widget.imageselect;

import java.io.Serializable;

public class ImageItem implements Serializable {

    public String imagePath;// 图片路径
    public String imageName;// 图片名字
    public boolean isCamera;// 是否是照片
    public boolean isDown;//是否是下载的图片
    public boolean isTakeCamera;//是否拍照
    public String imageUrl;
    public String imageUrlRaw;

    public ImageItem(String imagePath, String imageName) {
        super();
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    public ImageItem(String imagePath) {
        super();
        this.imagePath = imagePath;
    }

    public ImageItem(boolean isTakeCamera) {
        super();
        this.isTakeCamera = isTakeCamera;
    }

    public ImageItem() {
        super();
    }

    @Override
    public String toString() {
        return "ImageItem [imagePath=" + imagePath + ", imageName=" + imageName + "]";
    }


}
