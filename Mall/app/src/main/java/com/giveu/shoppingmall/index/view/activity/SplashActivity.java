package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class SplashActivity extends BasePermissionActivity {
    final int SPLASH_TIME = 700;
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
    Handler handler = new Handler();
    Map<String, String> permissionMap = new HashMap<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.layout_splash);
        ivSplash.setImageResource(R.drawable.splash);
       // ImageUtils.loadImage(ImageUtils.ImageLoaderType.drawable, R.drawable.splash + "", ivSplash);
        baseLayout.setTitleBarAndStatusBar(false, false);
        tvSkip.setVisibility(View.GONE);
        tvVersion.setText("V" + CommonUtils.getVersionName());
        needTurn = getIntent().getBooleanExtra(NEED_TURN_KEY, false);

        permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储权限");
        permissionMap.put(Manifest.permission.READ_PHONE_STATE, "电话权限");
        lastTimeMillis = System.currentTimeMillis();
        //6.0系统动态获取权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startCountTime();
        }
    }

    private void initPermissionDialog(String tips) {
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr(tips);
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                permissionDialog.dismiss();
            }

            @Override
            public void cancel() {
                permissionDialog.dismiss();
                finish();
            }
        });
    }


    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);

        startCountTime();
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull List<String> permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        //禁止不再询问会直接回调这方法
        if (!permissionName.isEmpty()) {
            String appName = getResources().getString(R.string.app_name);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(appName).append("需要开启");
            for (String name : permissionName) {
                stringBuffer.append(permissionMap.get(name));
                stringBuffer.append("，");
            }
            String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
            initPermissionDialog(substring + "才可正常使用");
            permissionDialog.show();
        }
    }

    @Override
    public void setData() {
    }


    private void startCountTime() {
        SharePrefUtil.getUUId();//拿到imei权限后，生成并存储唯一标识
        BaseApplication.getInstance().initTokenDaemon();//调用第一个接口

        //获取权限后停留在该界面已有一段时间,为避免进入下一界面时等待时间过长，
        // 所以获取权限后，handler的delay时间应该减少
        long currentTimeMillis = System.currentTimeMillis();
        long deltaTimeMillis = currentTimeMillis - lastTimeMillis;
        if (deltaTimeMillis > SPLASH_TIME) {
            deltaTimeMillis = SPLASH_TIME;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startViewPagerOrActivity();
            }
        }, SPLASH_TIME - deltaTimeMillis);
    }


    protected void startViewPagerOrActivity() {
        getAdSplashImage();
        if (hasEnterOtherActivity) {
            return;
        }
        if (SharePrefUtil.getNeedWelcome()) {
            WelcomeActivity.startIt(mBaseContext);
            //删除apk文件
            try {
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BaseApplication.getInstance().getPackageName();
                File file = new File(dirPath, "jiyouqianbao.apk");
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            AdSplashResponse adSplashImage = SharePrefUtil.getAdSplashImage();
            if (adSplashImage != null && StringUtils.isNotNull(adSplashImage.imgUrl)) {
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
    protected void onStart() {
        super.onStart();
        //6.0申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] a = new String[permissionMap.keySet().size()];
            permissionMap.keySet().toArray(a);
            setPermissionHelper(true, a);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void getAdSplashImage() {
        ApiImpl.AdSplashImage("1", new BaseRequestAgent.ResponseListener<AdSplashResponse>() {
            @Override
            public void onSuccess(AdSplashResponse response) {
                if (response.data != null) {
                    SharePrefUtil.setAdSplashImage(response.data);
                    String url = response.data.imgUrl;
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
