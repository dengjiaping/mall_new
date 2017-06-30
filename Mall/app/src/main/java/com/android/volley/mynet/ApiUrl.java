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
    String personCenter_account_register = BASE_URL + "personCenter/account/register";
    //下发短信验证码
    String personCenter_util_sendSMSCode = BASE_URL + "personCenter/util/sendSMSCode";
    //校验短信验证码
    String personCenter_util_chkValiCode = BASE_URL + "personCenter/util/chkValiCode";
    //用户登录
    String personCenter_account_login = BASE_URL + "personCenter/account/login";
    //找回登录密码（校验短信码）
    String personCenter_account_resetPwd_checkSmsCode = BASE_URL + "personCenter/account/resetPwd/checkSmsCode";
    //找回密码（重置密码）
    String personCenter_account_resetLoginPwd = BASE_URL + "personCenter/account/resetLoginPwd";


    //找回密码（校验身份）
    String personCenter_account_resetPwd_checkUserInfo = BASE_URL + "personCenter/account/resetPwd/checkUserInfo";
    //钱包激活
    String personCenter_account_activateWallet = BASE_URL + "personCenter/account/activateWallet";
    //修改手机号
    String personCenter_account_updatePhone = BASE_URL + "personCenter/account/updatePhone";
    //校验交易密码
    String personCenter_account_verifyPayPwd = BASE_URL + "personCenter/account/verifyPayPwd";
    //查询绑卡列表
    String personCenter_bankCard_getBankInfo = BASE_URL + "personCenter/bankCard/getBankInfo";
}
