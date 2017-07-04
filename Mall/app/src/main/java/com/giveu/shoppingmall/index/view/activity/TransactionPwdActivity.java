package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;

/**
 * 设置交易密码
 * Created by 101900 on 2017/6/20.
 */

public class TransactionPwdActivity extends BaseActivity {
    @BindView(R.id.tv_string)
    TextView tvString;

    String firstPwd;
    @BindView(R.id.input_view_pwd)
    PassWordInputView inputViewPwd;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_pwd);
        baseLayout.setTitle("设置交易密码");
        CommonUtils.openSoftKeyBoard(mBaseContext);
    }

    @Override
    public void setData() {

    }

    public void setListener() {
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (result.length() == 6) {
                    if (StringUtils.isNotNull(firstPwd)) {
                        //第二次输入密码
                        if (firstPwd.equals(result)) {
                            //第一次输入的交易密码MD5加密
                            String newPwd = MD5.MD5Encode(firstPwd);
                            if (!TextUtils.isEmpty(newPwd)) {
                                newPwd = newPwd.toLowerCase();
                            }
                            //第二次输入的交易密码MD5加密
                            String confirmPwd = MD5.MD5Encode(firstPwd);
                            if (!TextUtils.isEmpty(confirmPwd)) {
                                confirmPwd = confirmPwd.toLowerCase();
                            }
                            ApiImpl.resetPayPwd(mBaseContext, confirmPwd, "14703507", newPwd, new BaseRequestAgent.ResponseListener<BaseBean>() {
                                @Override
                                public void onSuccess(BaseBean response) {
                                    ActivationStatusActivity.startIt(mBaseContext, "100", null, null, null, "设置成功！请牢记你的交易密码");

                                }

                                @Override
                                public void onError(BaseBean errorBean) {
                                    CommonLoadingView.showErrorToast(errorBean);
                                }
                            });

                        } else {
                            ToastUtils.showShortToast("两次输入的密码不一致！");
                        }
                    } else {
                        //第一次输入密码
                        firstPwd = result;
                        inputViewPwd.clearResult();
                        tvString.setText("请再次设置交易密码");
                    }
                }
            }
        });
    }

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, TransactionPwdActivity.class);
        mActivity.startActivity(intent);
    }

}
