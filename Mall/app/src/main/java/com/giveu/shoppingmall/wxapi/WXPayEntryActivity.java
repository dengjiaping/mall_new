package com.giveu.shoppingmall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.utils.PayUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by 513419 on 2017/7/4.
 * 微信支付回调
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ToastUtils.showShortToast("微信支付回调");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = PayUtils.getWxApi();
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                if (BaseApplication.getInstance().getBeforePayActivity().equals(VerifyActivity.class.getSimpleName())) {
                    VerifyActivity.startItAfterPay(this, VerifyActivity.RECHARGE);
                }
            } else {
                ToastUtils.showShortToast("支付失败");
            }
            finish();
        }
    }
}
