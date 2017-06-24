package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionDetailActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, TransactionDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_detail);
        baseLayout.setTitle("交易详情");
    }

    @Override
    public void setData() {

    }

    @OnClick(R.id.ll_credit_detail)
    public void onCreditDetail() {
        CreditDetailActivity.startIt(mBaseContext);
    }
}
