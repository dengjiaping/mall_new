package com.giveu.shoppingmall.widget.imageselect;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.List;

/**
 * 大图预览
 *
 * @author Administrator
 */
public abstract class AbsImgPreviewActivity extends BaseActivity implements OnPageChangeListener {

    public FixedViewPager viewPager;
    public LinearLayout ll_back;
    public LinearLayout ll_right;
    public CheckBox check;
    public TextView txt_index;
    /**
     * 图片列表
     */
    public List<ImageItem> imageList;
    /**
     * 适配器
     */
    public ImagePreViewAdapter pagerAdapter;
    /**
     * 当前的索引
     */
    public int currIndex;

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void setListener() {
    }

    @Override
    public void setData() {
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_image_preview);
        baseLayout.setTitleBarAndStatusBar(false, true);

        init();
        afterInit();
        fillData();
    }

    private void init() {
        viewPager= (FixedViewPager) findViewById(R.id.subViewPager);
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        ll_right= (LinearLayout) findViewById(R.id.ll_right);
        check= (CheckBox) findViewById(R.id.check);
        txt_index= (TextView) findViewById(R.id.txt_index);

        ll_back.setOnClickListener(this);
        ll_right.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 设置数据
     */
    private void fillData() {
        if (!CommonUtils.isNullOrEmpty(imageList)) {
            if (pagerAdapter == null) {
                pagerAdapter = new ImagePreViewAdapter(this, imageList);
            }
            viewPager.setAdapter(pagerAdapter);
            if (currIndex < imageList.size()) {
                setTitleIndex(currIndex, imageList.size());
                if (currIndex == 0) {
                    onPageSelected(currIndex);
                } else {
                    viewPager.setCurrentItem(currIndex);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int poistion) {
        currIndex = poistion;
        setTitleIndex(currIndex, imageList.size());
        doPageSelected(poistion);
    }

    public void setTitleIndex(int index, int count){
        index = index+1;
        if (index > count){
            index = count;
        }
        txt_index.setText(index + "/" + count);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                back();
                break;
            case R.id.ll_right:
                onClickRight();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 获取 imageList，currIndex，
     */
    public abstract void afterInit();

    /**
     * 当滑动viewpager onPageSelected的操作
     */
    public abstract void doPageSelected(int poistion);

    /**
     * 返回操作
     */
    public abstract void back();

    /**
     * 点击右边操作
     */
    public abstract void onClickRight();
}
