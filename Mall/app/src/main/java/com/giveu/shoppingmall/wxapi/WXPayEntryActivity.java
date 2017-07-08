package com.giveu.shoppingmall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.WXPayUtil;
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
        api = WXPayUtil.getWxApi();
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
        LogUtil.e(baseReq.getType() + "");
        if (baseReq.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        ToastUtils.showShortToast("错误码"+baseResp.errCode);
        LogUtil.e("onPayFinish,errCode=" + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
    }
}
