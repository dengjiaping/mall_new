package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.widget.PassWordInputView;


/**
 * 验证交易密码的弹窗
 */
public class PwdDialog {
    private CustomDialog mDialog;
    public PassWordInputView inputView;
    Activity mActivity;
    private String payPwd;
    TextView tv_dialog_pwd;
    String mStatusType;

    public PwdDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_pwd, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
    }

    public PwdDialog(Activity mActivity, String statusType) {
        this.mActivity = mActivity;
        mStatusType = statusType;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_pwd, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dissmissDialog() {
        if (mActivity != null && !mActivity.isFinishing() && mDialog.isShowing())
            mDialog.dismiss();
    }


    private void initView(final View contentView) {
        CommonUtils.openSoftKeyBoard(mActivity);
        tv_dialog_pwd = (TextView) contentView.findViewById(R.id.tv_dialog_pwd);
        inputView = (PassWordInputView) contentView.findViewById(R.id.inputview_dialog);
        inputView.clearFocus();
    }

    public void setListener() {
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


        //忘记密码跳转
        tv_dialog_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestPasswordActivity.startIt(mActivity, RequestPasswordActivity.FIND_TRADE_PWD);
            }
        });
    }

    public interface OnCheckPwdListener {
        void checkPwd(String payPwd);
    }

    private OnCheckPwdListener checkPwdListener;

    public void setOnCheckPwdListener(OnCheckPwdListener checkPwdListener) {
        this.checkPwdListener = checkPwdListener;
    }

    public void showPwdError(int remainTimes) {
        PwdErrorDialog errorDialog = new PwdErrorDialog();
        errorDialog.showDialog(mActivity, remainTimes);
        inputView.clearResult();
    }

    //监听密码输入框dialog的关闭
    public PwdDialog setdismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDialog.setOnDismissListener(dismissListener);
        return this;
    }

    public interface statusType {
        String CASH = "cash";
        String RECHARGE = "recharge";
        String BANKCARD = "bankCard";
    }

}
