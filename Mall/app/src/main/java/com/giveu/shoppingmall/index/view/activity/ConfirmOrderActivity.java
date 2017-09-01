package com.giveu.shoppingmall.index.view.activity;

import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

/**
 * Created by 101900 on 2017/9/1.
 */

public class ConfirmOrderActivity extends BaseActivity {
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        baseLayout.setTitle("订单信息确认");
    }

    @Override
    public void setData() {

    }
}
