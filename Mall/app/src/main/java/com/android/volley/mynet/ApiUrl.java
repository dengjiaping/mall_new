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
    //钱包资质判定
    String personCenter_account_getWalletQualified = BASE_URL + "personCenter/account/getWalletQualified";
    //钱包激活发送短信接口
    String personCenter_account_sendActivateSmsCode = BASE_URL + "personCenter/account/sendActivateSmsCode";
    //钱包激活
    String personCenter_account_activateWallet = BASE_URL + "personCenter/account/activateWallet";
    //修改手机号
    String personCenter_account_updatePhone = BASE_URL + "personCenter/account/updatePhone";
    //设置(钱包激活)交易密码
    String personCenter_account_setPayPwd = BASE_URL + "personCenter/account/setPayPwd";
    //校验交易密码
    String personCenter_account_verifyPayPwd = BASE_URL + "personCenter/account/verifyPayPwd";
    //设置（重置）交易密码
    String personCenter_account_resetPayPwd = BASE_URL + "personCenter/account/resetPayPwd";
    //查询绑卡列表
    String personCenter_bankCard_getBankInfo = BASE_URL + "personCenter/bankCard/getBankInfo";
    //还款首页
    String personCenter_repayment_getRepaymentInfo = BASE_URL + "personCenter/repayment/getRepaymentInfo";
    //还款明细
    String personCenter_repayment_getInstalmentDetails = BASE_URL + "personCenter/repayment/getInstalmentDetails";
    //交易查询
    String personCenter_repayment_listContract = BASE_URL + "personCenter/repayment/listContract";
    //交易详细
    String personCenter_repayment_getContractDetails = BASE_URL + "personCenter/repayment/getContractDetails";
    //分期明细
    String personCenter_repayment_listInstalment = BASE_URL + "personCenter/repayment/listInstalment";
    //删除银行卡
    String personCenter_bankCard_deleteBankInfo = BASE_URL + "personCenter/bankCard/deleteBankInfo";
    //设置默认代扣卡
    String personCenter_bankCard_setDefaultCard = BASE_URL + "personCenter/bankCard/setDefaultCard";
    //获取用户信息
    String personCenter_account_getUserInfo = BASE_URL + "personCenter/account/getUserInfo";
    //分期产品取现费用计算
    String personCenter_enchashment_repaycost = BASE_URL + "personCenter/enchashment/repaycost";
    //随借随还取现费用计算
    String personCenter_enchashment_enchashment = BASE_URL + "personCenter/enchashment/enchashment";
    //话费流量充值产品
    String goods_callTraffics = BASE_URL + "goods/callTraffics";
    //手机归属地查询
    String goods_callTraffics_segment = BASE_URL + "goods/callTraffics/segment";
    //初始化金融产品接口
    String personCenter_enchashment_getProducts = BASE_URL + "personCenter/enchashment/getProducts";
    //分期取现月供明细
    String personCenter_enchashment_rpmDetail = BASE_URL + "personCenter/enchashment/rpmDetail";
    //创建充值订单
    String order_createRechargeOrder = BASE_URL + "order/createRechargeOrder";
    //确认充值订单
    String order_confirmRechargeOrder = BASE_URL + "order/confirmRechargeOrder";
    //第三方支付成功调用充值
    String order_thirdPayRecharge = BASE_URL + "order/thirdPayRecharge";
    //还款预下单
    String personCenter_repayment_createRepaymentOrder = BASE_URL + "personCenter/repayment/createRepaymentOrder";
}
