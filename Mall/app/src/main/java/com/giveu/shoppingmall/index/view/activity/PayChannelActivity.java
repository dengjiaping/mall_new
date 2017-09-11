package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.CountDownTextView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/9/11.
 * 支付方式
 */

public class PayChannelActivity extends BaseActivity {
    @BindView(R.id.tv_remain_time)
    CountDownTextView tvRemainTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.ll_pay_fail)
    LinearLayout llPayFail;
    @BindView(R.id.tv_back)
    TextView tvBack;
    private ConfirmDialog cancelDialog;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, PayChannelActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_channel);
        baseLayout.setTitle("支付首付金额");
        CommonUtils.setTextWithSpanSizeAndColor(tvMoney, "¥", StringUtils.format2(123 + ""), "",
                15, 12, R.color.color_00bbc0, R.color.color_00bbc0);
        cancelDialog = new ConfirmDialog(mBaseContext);
        cancelDialog.setContent("是否放弃支付?");
        cancelDialog.setConfirmStr("取消");
        cancelDialog.setCancleStr("确定");
        baseLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog.show();
            }
        });
    }

    @Override
    public void setListener() {
        super.setListener();
        cancelDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                cancelDialog.dismiss();
            }

            @Override
            public void cancle() {
                cancelDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_back, R.id.tv_confirm, R.id.tv_order})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_back:
                cancelDialog.show();
                break;
            case R.id.tv_confirm:
                break;
            case R.id.tv_order:
                OrderInfoActivity.startIt(mBaseContext, "", "");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        cancelDialog.show();
    }
}
