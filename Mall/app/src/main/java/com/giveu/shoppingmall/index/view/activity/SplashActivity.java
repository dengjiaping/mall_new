package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;


public class SplashActivity extends BasePermissionActivity {
    final int SPLASH_TIME = 1500;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    public static String NEED_TURN_KEY = "NEED_TURN_KEY";//是否需要跳转至消息列表（点击推送内容后的消息判断）
    @BindView(R.id.tv_version)
    TextView tvVersion;
    private boolean needTurn;
    private PermissionDialog permissionDialog;
    private long lastTimeMillis;
    private boolean hasEnterOtherActivity;
    //是否被用户禁止不再询问，设此标志位是因为onPermissionReallyDeclined
    //回调后会执行onResume方法，导致setPermissionHelper(true, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    //重复调用导致闪屏
    private boolean isPermissionReallyDeclined;

    @Override
    public void initView(Bundle savedInstanceState) {

        setContentView(R.layout.layout_splash);
        ImageUtils.loadImage(ImageUtils.ImageLoaderType.drawable, R.drawable.splash + "", ivSplash);
        baseLayout.setTitleBarAndStatusBar(false, false);
        tvSkip.setVisibility(View.GONE);
        needTurn = getIntent().getBooleanExtra(NEED_TURN_KEY, false);
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr(getResources().getString(R.string.app_name) + "需要存储权限才可正常使用");
        lastTimeMillis = System.currentTimeMillis();
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                //进入设置了，下次onResume时继续判断申请权限
                isPermissionReallyDeclined = false;
                permissionDialog.dismiss();
            }

            @Override
            public void cancle() {
                permissionDialog.dismiss();
                finish();
            }
        });
        //6.0系统动态获取权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startCountTime();
        }
        tvVersion.setText("V" + CommonUtils.getVersionName());
    }


    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);
        startCountTime();
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        //禁止不再询问会直接回调这方法
        isPermissionReallyDeclined = true;
        permissionDialog.show();
    }

    @Override
    public void setData() {
    }


    private void startCountTime() {
        //获取权限后停留在该界面已有一段时间,为避免进入下一界面时等待时间过长，
        // 所以获取权限后，handler的delay时间应该减少
        long currentTimeMillis = System.currentTimeMillis();
        long deltaTimeMillis = currentTimeMillis - lastTimeMillis;
        if (deltaTimeMillis > SPLASH_TIME) {
            deltaTimeMillis = SPLASH_TIME;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startViewPagerOrActivity();
            }
        }, SPLASH_TIME - deltaTimeMillis);
    }


    protected void startViewPagerOrActivity() {
//        getAdSplashImage();
        if (hasEnterOtherActivity) {
            return;
        }
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
        hasEnterOtherActivity = true;
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isPermissionReallyDeclined) {
                setPermissionHelper(true, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        }

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
