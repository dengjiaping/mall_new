package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;


/**
 * Created by 101900 on 2017/2/6.
 */

public class PwdErrorDialog {
    private CustomDialog mDialog;
    Activity mActivity;
    int times;
    TextView tv_error_text, tv_left_errorpwd, tv_right_errorpwd;

    public void showDialog(Activity activity, int times) {
        this.mActivity = activity;
        this.times = times;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_error_pwd, null);
        initView(contentView);

        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(final View contentView) {
        tv_error_text = (TextView) contentView.findViewById(R.id.tv_error_text);
        tv_left_errorpwd = (TextView) contentView.findViewById(R.id.tv_left_errorpwd);
        tv_right_errorpwd = (TextView) contentView.findViewById(R.id.tv_right_errorpwd);

        if (times == 0) {
            //错误达到3次
            tv_left_errorpwd.setVisibility(View.GONE);
            tv_right_errorpwd.setText("找回交易密码");
            tv_error_text.setText("您的交易密码连续输错3次！账号暂时被冻结");
        } else {
            tv_left_errorpwd.setVisibility(View.VISIBLE);
            tv_right_errorpwd.setText("重试");
            tv_error_text.setText("交易密码错误，您还剩下" + times + "次机会");
        }
        setListener();
    }

    public void setListener() {
        //忘记密码的跳转
        tv_left_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestPasswordActivity.startIt(mActivity,true);
            }
        });

        //重试(或找回交易密码)
        tv_right_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (times == 0) {
                    //跳转找回交易密码
                    RequestPasswordActivity.startIt(mActivity,true);
                }
                mDialog.dismiss();
            }
        });

    }

}
