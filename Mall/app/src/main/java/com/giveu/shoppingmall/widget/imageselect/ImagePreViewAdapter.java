package com.giveu.shoppingmall.widget.imageselect;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ImagePreViewAdapter extends PagerAdapter {

    private Context context;

    private DisplayImageOptions localBigPicOption;

    public ImagePreViewAdapter(Context context, List<ImageItem> imageList) {
        this.context = context;
        setImageList(imageList);

        localBigPicOption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.pic_imagefaile)
                .showImageOnFail(R.drawable.pic_imagefaile)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    List<ImageItem> imageList;

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.inc_photoviewer, null);
        final ImageView photoView = (ImageView) view.findViewById(R.id.iv_photoviewer_image);
        final ProgressBar bar = (ProgressBar) view.findViewById(R.id.progress_photoviewer);
        final ImageItem imageItem = imageList.get(position);
        LogUtils.i("imageItem.imagePath:" + imageItem.imagePath);

        ImageUtils.loadImage("file://" + imageItem.imagePath, photoView, localBigPicOption, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                bar.setVisibility(View.GONE);
                LogUtils.i("onLoadingFailed:" + imageItem.imagePath);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                bar.setVisibility(View.GONE);
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
