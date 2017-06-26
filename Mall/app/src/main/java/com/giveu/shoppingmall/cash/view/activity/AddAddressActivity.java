package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddAddressActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_address);
        baseLayout.setTitle("订单信息确认");
        baseLayout.setRightTextColor(R.color.color_00bbc0);
        baseLayout.setRightTextAndListener("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void setData() {

    }
}
