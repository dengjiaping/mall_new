package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * 联系我们
 * Created by 101900 on 2017/6/26.
 */

public class ContactUsActivity extends BaseActivity {

    public static void startIt(Activity mActivity){
        Intent intent = new Intent(mActivity,ContactUsActivity.class);
        mActivity.startActivity(intent);
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public void setData() {

    }
}
