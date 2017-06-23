package com.giveu.shoppingmall.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 客服电话页面
 * Created by 101900 on 2017/6/23.
 */

public class CustomerServicePhoneActivity extends BaseActivity {
    @BindView(R.id.tv_call_phone)
    TextView tvCallPhone;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_customer_service_phone);
        baseLayout.setTitle("客服电话");

    }

    @Override
    public void setData() {

    }

    @OnClick(R.id.tv_call_phone)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        CommonUtils.callPhone(mBaseContext, getResources().getString(R.string.customer_phone));
    }
}
