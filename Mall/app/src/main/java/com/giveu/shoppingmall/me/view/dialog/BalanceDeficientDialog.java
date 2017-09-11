package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 101912 on 2017/9/9.
 * 钱包余额不足的弹框
 */

public class BalanceDeficientDialog {

    private CustomDialog mDialog;
    Activity mActivity;
    TextView tvContent, tvConfirm;

    public BalanceDeficientDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_balance_deficiency, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
    }

    private void initView(View view) {
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
    }

    private void setListener() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void show() {
        mDialog.show();
    }


    //设置钱包余额
    public void setBalance(String balance) {
        SpannableString content = new SpannableString("您的钱包可用消费余额¥" + balance);
        ForegroundColorSpan greySpan = new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(R.color.color_767876));
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(R.color.color_00bbc0));
        content.setSpan(greySpan, 0, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//从起始下标到终了下标，包括起始下标
        content.setSpan(blueSpan, 10, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.0f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.5f);
        content.setSpan(sizeSpan01, 0, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        content.setSpan(sizeSpan02, 10, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContent.setText(content);
    }

}
