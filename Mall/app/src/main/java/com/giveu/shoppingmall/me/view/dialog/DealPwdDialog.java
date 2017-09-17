package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;

/**
 * Created by 101912 on 2017/9/9.
 * 商城付款验证交易密码的dialog
 */


public class DealPwdDialog {

    private CustomDialog mDialog;
    public PassWordInputView inputView;
    Activity mActivity;
    private String payPwd;
    TextView tvForgetPwd, tvPrice;
    ImageView ivDismiss;


    public DealPwdDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_deal_pwd, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
    }


    private void initView(View view) {
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvForgetPwd = (TextView) view.findViewById(R.id.tv_forget_pwd);
        inputView = (PassWordInputView) view.findViewById(R.id.inputview_dialog);
        ivDismiss = (ImageView) view.findViewById(R.id.iv_dismiss);
        inputView.clearFocus();
    }

    private void setListener() {
        inputView.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (result.length() == 6) {
                    String tradPwd = MD5.MD5Encode(result);
                    if (!TextUtils.isEmpty(tradPwd)) {
                        payPwd = tradPwd.toLowerCase();
                    }
                    CommonUtils.closeSoftKeyBoard(inputView.getWindowToken(), mActivity);
                    if (checkPwdListener != null) {
                        checkPwdListener.checkPwd(payPwd);
                        inputView.clearResult();
                    }
                }
            }
        });

        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //忘记密码跳转
                RequestPasswordActivity.startIt(mActivity, RequestPasswordActivity.FIND_TRADE_PWD);
            }
        });

        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void setPrice(String price) {
        CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(price), "", 28, 19, R.color.color_00BBC0, R.color.color_4a4a4a);
    }

    //密码验证错误时显示错误弹框
    public void showPwdError(int remainTimes) {
        ErrorPwdDialog errorDialog = new ErrorPwdDialog();
        errorDialog.showDialog(mActivity, remainTimes);
        if (remainTimes == 0) {
            dissmissDialog();
        }
        inputView.clearResult();
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dissmissDialog() {
        if (mActivity != null && !mActivity.isFinishing() && mDialog.isShowing())
            mDialog.dismiss();
    }


    private OnCheckPwdListener checkPwdListener;

    public void setOnCheckPwdListener(OnCheckPwdListener checkPwdListener) {
        this.checkPwdListener = checkPwdListener;
    }

    //验证密码是否正确
    public interface OnCheckPwdListener {
        void checkPwd(String payPwd);
    }

}
