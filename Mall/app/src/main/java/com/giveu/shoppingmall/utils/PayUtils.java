package com.giveu.shoppingmall.utils;

import com.giveu.shoppingmall.base.BaseApplication;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
}
