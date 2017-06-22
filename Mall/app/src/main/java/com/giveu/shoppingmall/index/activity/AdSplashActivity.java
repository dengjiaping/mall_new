package com.giveu.shoppingmall.index.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101900 on 2017/1/20.
 * 打开app时候的广告闪屏页面
 */

public class AdSplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_skip)
    TextView tvSkip;

    final int MAXCOUNT = 5;
    int count = MAXCOUNT;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count--;
            tvSkip.setText("跳过("+ count +")");
            if (count == 0){
                turnToMainActivity();
            }else{
                handler.postDelayed(this, 1000);
            }
        }
    };

    public void startCount(){
        handler.postDelayed(runnable, 0);
    }

    public void stopCount(){
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }


    private void turnToMainActivity() {
        stopCount();
        if (LoginHelper.getInstance().hasLogin()) {
            MainActivity.startIt(mBaseContext);
        } else {
//            CommonUtils.startActivity(mBaseContext, LoginActivity.class);
        }
        finish();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.layout_splash);
        baseLayout.setTitleBarAndStatusBar(false, false);

        startCount();
    }

    @Override
    public void setData() {
        AdSplashResponse adSplashImage = SharePrefUtil.getAdSplashImage();
        if (adSplashImage != null && StringUtils.isNotNull(adSplashImage.imageUrl)) {
            ImageUtils.loadImage(adSplashImage.imageUrl, ivSplash);
        }
    }

    @OnClick(R.id.tv_skip)
    public void onSkipAd() {
        turnToMainActivity();
    }

    @OnClick(R.id.iv_splash)
    public void onClickAd() {
        AdSplashResponse adSplashImage = SharePrefUtil.getAdSplashImage();
        if (adSplashImage != null && StringUtils.isNotNull(adSplashImage.respondUrl)) {

        }
    }

    public static void startIt(Activity activity){
        Intent intent = new Intent(activity, AdSplashActivity.class);
        activity.startActivity(intent);
    }


}
