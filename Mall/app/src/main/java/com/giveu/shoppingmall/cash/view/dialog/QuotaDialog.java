package com.giveu.shoppingmall.cash.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;


/**
 * 取现额度为0的弹窗
 * Created by 101900 on 2017/3/6.
 */

public class QuotaDialog {
    private CustomDialog mDialog;
    private Activity mActivity;

    public QuotaDialog(Activity activity) {
        mActivity = activity;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_quota, null);
        initView(convertView);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
    }

    public void showDialog() {
        mDialog.show();
    }

    public void initView(View convertView){
        ClickEnabledTextView tv_back = (ClickEnabledTextView) convertView.findViewById(R.id.tv_back);
        tv_back.setBackgroundResource(R.drawable.selector_login);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }
}
