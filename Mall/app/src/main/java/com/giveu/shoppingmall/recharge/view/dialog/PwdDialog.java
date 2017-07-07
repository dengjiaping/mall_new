package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;


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


    private void initView(final View contentView) {
        CommonUtils.openSoftKeyBoard(mActivity);
        tv_dialog_pwd = (TextView) contentView.findViewById(R.id.tv_dialog_pwd);
        inputView = (PassWordInputView) contentView.findViewById(R.id.inputview_dialog);

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
                    turnToSuccessActivity();
                }
            }
        });


        //忘记密码跳转
        tv_dialog_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestPasswordActivity.startIt(mActivity, true);
            }
        });
    }

    //控制校验密码，2 失败  1 成功

    public void turnToSuccessActivity() {
        ApiImpl.verifyPayPwd(mActivity, "11413713", payPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
            @Override
            public void onSuccess(PayPwdResponse response) {
                if (response.data != null) {
                    PayPwdResponse pwdResponse = response.data;
                    if (pwdResponse.status) {
                        //密码正确
                        switch (mStatusType) {
                            case statusType.BANKCARD:
                                //银行卡换默认代扣卡不需要验证手机
                                if (listener != null) {
                                    listener.onSuccess(pwdResponse.code);
                                }
                                break;
                            case statusType.CASH:
                            case statusType.RECHARGE:
                                //取现充值需要验证手机
                                VerifyActivity.startIt(mActivity, mStatusType);
                                break;
                        }
                        mDialog.dismiss();
                        inputView.clearResult();
                    } else {
                        //TODO: 1-2 重试密码 3 冻结密码需要找回密码
                        PwdErrorDialog errorDialog = new PwdErrorDialog();
                        errorDialog.showDialog(mActivity, pwdResponse.remainTimes);
                        inputView.clearResult();
                    }

                    CommonUtils.closeSoftKeyBoard(mActivity);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    /**
     * 验证成功的监听
     */
    private OnVerifyPwdListener listener;

    public interface OnVerifyPwdListener {
        void onSuccess(String code);
    }

    public void setOnVerifyPwdListener(OnVerifyPwdListener listener) {
        this.listener = listener;
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
