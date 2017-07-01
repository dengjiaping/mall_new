package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;


/**
 * 验证交易密码的弹窗
 */
public class PwdDialog {
    private CustomDialog mDialog;
    public PassWordInputView inputView;
    Activity mActivity;
    private String oldPwd;
    // CallsParam callsParam;
    TextView tv_dialog_pwd;

    public PwdDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_pwd, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        // CommonUtils.openSoftKeyBoard(mActivity);
    }

    public void showDialog() {
        mDialog.show();
    }


    private void initView(final View contentView) {

        tv_dialog_pwd = (TextView) contentView.findViewById(R.id.tv_dialog_pwd);
        inputView = (PassWordInputView) contentView.findViewById(R.id.inputview_dialog);

    }

    public void setListener() {
        inputView.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (result.length() == 6) {
                    oldPwd = result;
                    CommonUtils.closeSoftKeyBoard(inputView.getWindowToken(), mActivity);
                    turnToSuccessActivity();
                }
            }
        });


        //忘记密码跳转
        tv_dialog_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }

    //控制校验密码，2 失败  1 成功

    public void turnToSuccessActivity() {
        if (oldPwd.equals("111111")) {
            VerifyActivity.startIt(mActivity);
            mDialog.dismiss();
            inputView.clearResult();
        } else {
            //1-2 重试密码 3 冻结密码需要找回密码
            PwdErrorDialog errorDialog = new PwdErrorDialog();
            errorDialog.showDialog(mActivity, 3);
            inputView.clearResult();
        }

//        ApiImpl.checkPwd(mActivity, oldPwd, "", new ResponseListener<BaseBean>() {
//            @Override
//            public void onSuccess(BaseBean response) {
//                if (listener != null) {
//                    listener.onSuccess(null);
//                } else {
//                    turnToRecharge();
//                }
//                mDialog.dismiss();
//            }
//
//            @Override
//            public void onError(BaseBean errorBean) {
//                PwdErrorDialog errorDialog = new PwdErrorDialog();
//                errorDialog.showDialog(mActivity);
//                inputView.clearResult();
//
//            }
//        });
    }

    //监听密码输入框dialog的关闭
    public PwdDialog setdismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDialog.setOnDismissListener(dismissListener);
        return this;
    }

    private void turnToRecharge() {

//        //0话费 1流量
//        if (callsParam.tabIndex == 0) {
//            //充话费
//            ApiImpl.rechargeCalls(mActivity, "0", callsParam.discountPrice, callsParam.mobile, callsParam.fvId, callsParam.name, callsParam.oper, callsParam.pid, callsParam.price, callsParam.product, "", new ResponseListener<BaseBean>() {
//                @Override
//                public void onSuccess(BaseBean response) {
//                    ToastUtils.showLongToast("成功");
//                    mActivity.finish();
//                    turnBillPaySuccessActivity();
//                }
//
//                @Override
//                public void onError(BaseBean errorBean) {
//                    NormalHintDialog hintDialog = new NormalHintDialog(mActivity, errorBean.message);
//                    hintDialog.showDialog();
//                }
//            });
//
//        } else {
//            //充流量
//            ApiImpl.rechargeFlows(mActivity, "0", callsParam.discountPrice, callsParam.mobile, callsParam.fvId, callsParam.name, callsParam.oper, callsParam.pid, callsParam.price, callsParam.product, "", new ResponseListener<BaseBean>() {
//                @Override
//                public void onSuccess(BaseBean response) {
//                    ToastUtils.showLongToast("成功");
//                    mActivity.finish();
//                    turnBillPaySuccessActivity();
//                }
//
//                @Override
//                public void onError(BaseBean errorBean) {
//                    NormalHintDialog hintDialog = new NormalHintDialog(mActivity, errorBean.message);
//                    hintDialog.showDialog();
//                }
//            });
//        }
    }

}
