package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * Created by 513419 on 2017/6/20.
 */

public class SetPasswordActivity extends BaseActivity {

    private boolean isSetPassword;

    public static void startIt(Activity activity,boolean isSetPassword){
        Intent intent = new Intent(activity,SetPasswordActivity.class);
        intent.putExtra("isSetPassword",isSetPassword);
        activity.startActivity(intent);
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        isSetPassword = getIntent().getBooleanExtra("isSetPassword",false);
        //区分是设置密码还是重置密码
        if(isSetPassword){
            baseLayout.setTitle("设置登录密码");
        }else {
            baseLayout.setTitle("重置登录密码");
        }
    }

    @Override
    public void setData() {

    }
}
