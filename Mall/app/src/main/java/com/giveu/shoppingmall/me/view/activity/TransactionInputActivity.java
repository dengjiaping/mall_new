package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdErrorDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;

/**
 * 通用的交易密码输入页面
 * Created by 101900 on 2017/6/29.
 */

public class TransactionInputActivity extends BaseActivity {

    @BindView(R.id.input_view_pwd)
    PassWordInputView inputViewPwd;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, TransactionInputActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_pwd);
        baseLayout.setTitle("修改手机号");
        //仅支持数字
        inputViewPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if(result.length() == 6){
                    //交易密码MD5加密
                    String tradPwd = MD5.MD5Encode(result);
                    if (StringUtils.isNotNull(tradPwd)){
                        tradPwd = tradPwd.toLowerCase();
                        ApiImpl.verifyPayPwd(mBaseContext, LoginHelper.getInstance().getIdPerson(), tradPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
                            @Override
                            public void onSuccess(PayPwdResponse response) {
                                if (response.data != null) {
                                    PayPwdResponse pwdResponse = response.data;
                                    if (pwdResponse.status) {
                                        //密码正确
                                        CommonUtils.closeSoftKeyBoard(mBaseContext);
                                        ChangePhoneNumberActivity.startIt(mBaseContext, response.data.code);
                                        finish();
                                    } else {
                                        //1-2 还剩输入次数 3 冻结密码需要找回密码
                                        PwdErrorDialog errorDialog = new PwdErrorDialog();
                                        errorDialog.showDialog(mBaseContext, pwdResponse.remainTimes);
                                        inputViewPwd.clearResult();
                                    }

                                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                                }
                            }

                            @Override
                            public void onError(BaseBean errorBean) {
                                CommonLoadingView.showErrorToast(errorBean);
                            }
                        });
                    }else{
                        ToastUtils.showShortToast("请输入交易密码！");
                    }



                }
            }
        });
    }

    @Override
    public void setData() {

    }

}
