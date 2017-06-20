package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/20.
 * 找回登录密码
 */

public class RequestPasswordActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RequestPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_password);
        baseLayout.setTitle("找回登录密码");
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_get_code, R.id.tv_next, R.id.tv_unreceived})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_get_code:
                break;
            case R.id.tv_next:
                SetPasswordActivity.startIt(mBaseContext, false);
                break;
            case R.id.tv_unreceived:
                break;
        }
    }
}
