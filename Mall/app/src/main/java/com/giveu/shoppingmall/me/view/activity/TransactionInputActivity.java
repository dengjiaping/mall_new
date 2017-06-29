package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;

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
        CommonUtils.openSoftKeyBoard(mBaseContext);
    }

    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if(result.length() == 6){
                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                    ChangePhoneNumberActivity.startIt(mBaseContext);
                }
            }
        });
    }

    @Override
    public void setData() {

    }

}
