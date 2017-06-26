package com.giveu.shoppingmall.index.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;


public class SplashActivity extends BaseActivity {
    final int SPLASH_TIME = 1500;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    public static String NEED_TURN_KEY = "NEED_TURN_KEY";//是否需要跳转至消息列表（点击推送内容后的消息判断）
    private boolean needTurn;

    @Override
    public void initView(Bundle savedInstanceState) {

        setContentView(R.layout.layout_splash);
        ImageUtils.loadImage(ImageUtils.ImageLoaderType.drawable, R.drawable.splash + "", ivSplash);
        baseLayout.setTitleBarAndStatusBar(false, false);
        tvSkip.setVisibility(View.GONE);
        needTurn = getIntent().getBooleanExtra(NEED_TURN_KEY, false);

        startCountTime();
    }


    @Override
    public void setData() {

    }


    private void startCountTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startViewPagerOrActivity();
            }
        }, SPLASH_TIME);
    }


    protected void startViewPagerOrActivity() {
//        getAdSplashImage();

        if (SharePrefUtil.getNeedWelcome()) {
            WelcomeActivity.startIt(mBaseContext);
        } else {
            AdSplashResponse adSplashImage = SharePrefUtil.getAdSplashImage();
            if (adSplashImage != null && StringUtils.isNotNull(adSplashImage.imageUrl)) {
                //有广告
                AdSplashActivity.startIt(mBaseContext);
            } else {
                //由于有可能极光推送点击跳转至的闪屏页，这里需要把是否需要跳转至消息列表的值传给MainActivity
                MainActivity.startItDealLock(0, mBaseContext, SplashActivity.class.getName(), needTurn);
            }
        }
        finish();
    }




    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void getAdSplashImage() {
        ApiImpl.AdSplashImage(null, new BaseRequestAgent.ResponseListener<AdSplashResponse>() {
            @Override
            public void onSuccess(AdSplashResponse response) {
                if (response.data != null) {
                    SharePrefUtil.setAdSplashImage(response.data);
                    String url = response.data.imageUrl;
                    if (StringUtils.isNotNull(url)) {
                        ImageUtils.loadImage(url, new ImageView(mBaseContext));
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });

    }


}
