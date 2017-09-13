package com.giveu.shoppingmall.utils;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.giveu.shoppingmall.base.BaseApplication;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by 513419 on 2017/7/4.
 * 微信支付，单例实现
 */
public class PayUtils {
    private static IWXAPI wxApi;
    private static final String APP_ID = "wxf3536093a5f1ce76";
    public static final String WX = "WECHAT_APP";

    private PayUtils() {

    }

    public static void init() {
        wxApi = WXAPIFactory.createWXAPI(BaseApplication.getInstance(), APP_ID, false);
        wxApi.registerApp(APP_ID);
    }

    public static IWXAPI getWxApi() {
        if (null == wxApi) {
            init();
        }
        return wxApi;
    }

    /**
     * 获取微信支付请求体
     *
     * @param partnerId
     * @param prepayId
     * @param packageValue
     * @param nonceStr
     * @param timeStamp
     * @param sign
     * @return
     */
    public static PayReq getRayReq(String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign) {
        PayReq request = new PayReq();
        request.appId = APP_ID;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        return request;
    }

    /**
     * 支付宝支付
     *
     * @param activity
     * @param orderInfo 订单信息
     * @param listener  支付结果回调
     */
    public static void AliPay(Activity activity, String orderInfo, AliPayThread.OnPayListener listener) {
        //支付宝支付是一个异步过程
        AliPayThread payThread = new AliPayThread(activity, orderInfo, listener);
        payThread.start();
    }

    public static class AliPayThread extends Thread {
        private WeakReference<Activity> activity;
        private String orderInfo;
        private OnPayListener listener;

        public AliPayThread(Activity activity, String orderInfo, OnPayListener listener) {
            this.activity = new WeakReference<Activity>(activity);
            this.orderInfo = orderInfo;
            this.listener = listener;
        }

        @Override
        public void run() {
            super.run();
            PayTask alipay = new PayTask(activity.get());
            final Map<String, String> result = alipay.payV2(orderInfo, true);
            if (listener != null) {
                if (activity.get() != null) {
                    //支付结果
                    activity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resultCode = result.get("resultStatus");
                            switch (resultCode) {
                                //支付成功
                                case "9000":
                                    listener.onSuccess();
                                    break;
                                //取消支付
                                case "6001":
                                    listener.onCancel();
                                    break;
                                //支付失败
                                default:
                                    listener.onFail();
                                    break;
                            }
                        }
                    });
                }
            }
        }

        public interface OnPayListener {
            void onSuccess();

            void onFail();

            void onCancel();
        }

    }


}
