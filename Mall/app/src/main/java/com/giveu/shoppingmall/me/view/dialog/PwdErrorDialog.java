package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;


/**
 * Created by 101900 on 2017/2/6.
 */

public class PwdErrorDialog {
    private CustomDialog mDialog;
    Activity mActivity;

    public void showDialog(Activity activity) {
        this.mActivity = activity;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_errorpwd, null);
        initView(contentView);

        mDialog = new CustomDialog(mActivity, contentView, R.style.date_dialog_style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
        mDialog.show();


    }

    private void initView(final View contentView) {
        TextView tv_left_errorpwd = (TextView) contentView.findViewById(R.id.tv_left_errorpwd);
        TextView tv_right_errorpwd = (TextView) contentView.findViewById(R.id.tv_right_errorpwd);


    //忘记密码的跳转
        tv_left_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToForgetPwdActivity();
            }
        });

        //重试
        tv_right_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

    }

    //忘记密码的跳转
    private void turnToForgetPwdActivity() {

    }
}
