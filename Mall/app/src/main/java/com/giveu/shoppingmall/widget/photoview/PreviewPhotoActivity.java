package com.giveu.shoppingmall.widget.photoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.widget.imageselect.FixedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by 513419 on 2017/3/3.
 */


public class PreviewPhotoActivity extends BaseActivity {
    @BindView(R.id.vp_Photo)
    FixedViewPager vp_Photo;

    private ArrayList<String> photoList;
    private int currentPos;
    private String title;

    public static void startIt(Activity activity, String title, ArrayList<String> photoList, int currentPos) {
        Intent intent = new Intent(activity, PreviewPhotoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("photoList", photoList);
        intent.putExtra("currentPos", currentPos);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_preview_photo);
        title = getIntent().getStringExtra("title");
        photoList = getIntent().getStringArrayListExtra("photoList");
        currentPos = getIntent().getIntExtra("currentPos", 0);
        if (photoList == null || photoList.isEmpty()) {
            //数据为空则关闭页面
            finish();
        }
        baseLayout.setTitle(String.format(title + "(%s/%s)", currentPos + 1, photoList.size()));
        vp_Photo.setAdapter(new PreviewPhotoAdapter(photoList));
        vp_Photo.setPageMargin(DensityUtils.dip2px(15));
        vp_Photo.setCurrentItem(currentPos);
        vp_Photo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                baseLayout.setTitle(String.format(title + "(%s/%s)", position + 1, photoList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setData() {

    }

    class PreviewPhotoAdapter extends PagerAdapter {
        private ArrayList<String> photoList;

        public PreviewPhotoAdapter(ArrayList<String> photoList) {
            this.photoList = photoList;
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView ivPhoto = new PhotoView(mBaseContext);
//            ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ivPhoto.setLayoutParams(layoutParams);
            if (isNetWorkUrl(photoList.get(position))) {
                ImageUtils.loadImage(photoList.get(position), ivPhoto);
            } else {
                ImageUtils.loadImage("file://" + photoList.get(position), ivPhoto);
            }
            container.addView(ivPhoto);
            return ivPhoto;
        }
    }

    /**
     * 判断是否网络图片url ，如果不是则默认其为本地图片
     */
    private boolean isNetWorkUrl(String url) {

        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            return true;
        }
        return false;
    }
}
