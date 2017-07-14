package com.giveu.shoppingmall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.event.RechargePayEvent;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.utils.EventBusUtils;
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
            //支付成功
            if (baseResp.errCode == 0) {
                //进入微信支付的上一个界面,这个是充值界面过来的
                if (BaseApplication.getInstance().getBeforePayActivity().equals(MainActivity.class.getSimpleName())) {
                    //充值成功，发送通知
                    EventBusUtils.poseEvent(new RechargePayEvent(true));
                } else if (BaseApplication.getInstance().getBeforePayActivity().equals(RepaymentActivity.class.getSimpleName())) {
                    //还款过来的
                    RepaymentActivity.startItAfterPay(this);
                }
            } else {
                if (BaseApplication.getInstance().getBeforePayActivity().equals(MainActivity.class.getSimpleName())) {
                    if (baseResp.errCode == -2) {
                        ToastUtils.showShortToast("支付取消");
                    } else {
                        //充值失败
                        EventBusUtils.poseEvent(new RechargePayEvent(false));
                    }
                }
                if (BaseApplication.getInstance().getBeforePayActivity().equals(RepaymentActivity.class.getSimpleName())) {
                    if (baseResp.errCode == -2) {
                        ToastUtils.showShortToast("支付取消");
                    } else {
                        ToastUtils.showShortToast("支付失败");
                    }
                }
            }
            BaseApplication.getInstance().setBeforePayActivity("");
            finish();
        }
    }
}
