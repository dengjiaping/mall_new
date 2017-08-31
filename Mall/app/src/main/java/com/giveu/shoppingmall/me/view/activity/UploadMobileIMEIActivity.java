package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * Created by 101912 on 2017/8/29.
 */

public class UploadMobileIMEIActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, UploadMobileIMEIActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_upload_mobile_imei);
        baseLayout.setTitle("上传手机串码");
    }

    @Override
    public void setData() {

    }
}
