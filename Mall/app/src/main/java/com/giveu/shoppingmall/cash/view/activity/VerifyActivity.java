package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/26.
 */

public class VerifyActivity extends BaseActivity {

    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify);
        baseLayout.setTitle("验证");
        tvSendCode.startCount(null);
    }

    @OnClick({R.id.tv_send_code})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                tvSendCode.startCount(null);
                break;
        }
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }
}
