package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.recharge.view.activity.RechargeStatusActivity;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.SendCodeTextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/26.
 */

public class VerifyActivity extends BaseActivity {

    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.input_view_pwd)
    PassWordInputView inputViewPwd;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify);
        baseLayout.setTitle("验证");
        tvSendCode.startCount(null);
        tvSendCode.setSendTextColor(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if(6 == result.length()){
                    if("123456".equals(result)){
                        Random random = new Random();
                        int randomNum = random.nextInt(2) + 1;
                        switch (randomNum) {
                            case 1:
                                RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", "100.00元", "98.00元", null);
                                break;
                            case 2:
                                RechargeStatusActivity.startIt(mBaseContext, "success", null, "100.00元", "98.00元", "温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
                                break;
                        }
                        finish();
                    }else{
                        ToastUtils.showShortToast("验证码错误");
                    }
                }
            }
        });
    }

    @OnClick({R.id.tv_send_code})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                tvSendCode.startCount(null);
                break;
        }
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }
}
