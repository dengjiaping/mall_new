package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/19.
 */

public class RegisterActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        baseLayout.setTitle("注册");
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_next})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_next:
                SetPasswordActivity.startIt(mBaseContext, true);
                break;
        }
    }
}
