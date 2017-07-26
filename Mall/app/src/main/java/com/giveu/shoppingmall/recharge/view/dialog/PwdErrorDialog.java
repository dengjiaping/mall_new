package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
            tv_right_errorpwd.setText("忘记密码");
            tv_error_text.setText("交易密码多次输入错误，请通过忘记密码找回");
        } else {
            tv_left_errorpwd.setVisibility(View.VISIBLE);
            tv_right_errorpwd.setText("重新输入");
            String startStr = "交易密码不正确，你还可以再输入";
            String endStr = "次";
            SpannableString msp = new SpannableString(startStr + times + endStr);
            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.color_4a4a4a)), 0, startStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.color_00adb2)), startStr.length(), startStr.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.color_4a4a4a)), startStr.length() + 1, startStr.length()+endStr.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_error_text.setText(msp);
        }
        setListener();
    }

    public void setListener() {
        //忘记密码的跳转
        tv_left_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestPasswordActivity.startIt(mActivity,RequestPasswordActivity.FIND_TRADE_PWD);
            }
        });

        //重试(或找回交易密码)
        tv_right_errorpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (times == 0) {
                    //跳转找回交易密码
                    RequestPasswordActivity.startIt(mActivity,RequestPasswordActivity.FIND_TRADE_PWD);
                }
                mDialog.dismiss();
            }
        });

    }

}
