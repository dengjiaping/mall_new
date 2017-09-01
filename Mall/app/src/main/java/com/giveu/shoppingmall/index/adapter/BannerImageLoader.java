package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by 513419 on 2017/8/30.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageUtils.loadImage((String) path, R.drawable.defalut_img_88_88, imageView);
    }
}
