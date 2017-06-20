package com.giveu.shoppingmall.index.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * Created by 101900 on 2017/6/19.
 */

public class ActivationStatusActivity extends BaseActivity {
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activation_status);
    }

    @Override
    public void setData() {

    }

    public static void startIt(Activity mActivity){
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        mActivity.startActivity(intent);
    }

}
