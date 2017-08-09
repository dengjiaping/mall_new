package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/8/9.
 * 居住地址
 */

public class LivingAddressActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_detail_address)
    EditText etDetailAddress;
    @BindView(R.id.tv_commit)
    ClickEnabledTextView tvCommit;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, LivingAddressActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_living_address);
        baseLayout.setTitle("我的居住地址");

    }

    @Override
    public void setData() {

    }

}
