package com.android.volley.mynet;

import com.giveu.shoppingmall.base.DebugConfig;

public interface ApiUrl {

    /**
     * 线上地址前缀
     */
    String BASE_URL = DebugConfig.getBaseUrl();
    String H5_BASE_URL = DebugConfig.getH5BaseUrl();

    interface WebUrl {
        String zhi_ma_xin_yong = "http://zhima.dafysz.com/Home/SignIn?idenNo=%s&name=%s&idCredit=0&deviceType=0";//芝麻信用
    }

    //广告页
    String activity_getImageInfo = BASE_URL + "activity/getImageInfo";
    String token_getToken = BASE_URL + "personCenter/token/applyToken";
    //验证密码
    String sales_account_checkPwd = BASE_URL + "sales/account/checkPwd";
    //登录
    String sales_account_login = BASE_URL + "sales/account/login";
    //公用请求蜂鸟接口
    String common_url = BASE_URL + "sales/public/commonsMethod";
    //公用请求蜂鸟path对应的key
    String COMMON_KEY = "commonsMethod_path";
    //保存极光设备号
    String api_Account_SaveDeviceNumber = "/api/Account/SaveDeviceNumber";
    //用户注册
    String v1_personCenter_account_register = BASE_URL + "/v1/personCenter/account/register";
    //下发短信验证码
    String v1_personCenter_util_sendSMSCode = BASE_URL + "/v1/personCenter/util/sendSMSCode";
    //校验短信验证码
    String v1_personCenter_util_chkValiCode = "/v1/personCenter/util/chkValiCode";


}
