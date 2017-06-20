package com.giveu.shoppingmall.index.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.view.InputView;

import butterknife.BindView;

/**
 * 设置交易密码
 * Created by 101900 on 2017/6/20.
 */

public class TransactionPwdActivity extends BaseActivity {
    @BindView(R.id.tv_string)
    TextView tvString;
    @BindView(R.id.inputview_pwd)
    InputView inputviewPwd;
    String firstPwd;
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
        inputviewPwd.setInputCallBack(new InputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (result.length() == 6) {
                    if(StringUtils.isNotNull(firstPwd)){
                        //第二次输入密码
                        if(firstPwd.equals(result)){
                            ActivationStatusActivity.startIt(mBaseContext,"100",null,null,null,null,"设置成功！请牢记你的交易密码");
                            finish();
                        }else{
                            ToastUtils.showShortToast("两次输入的密码不一致！");
                        }
                    }else{
                        //第一次输入密码
                        firstPwd = result;
                        inputviewPwd.clearResult();
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
