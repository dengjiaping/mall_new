package com.giveu.shoppingmall.base;


import android.text.TextUtils;

import com.giveu.shoppingmall.utils.sharePref.DevSettingSharePref;

/**
 * 这是调试变量，你可以修改
 */
public class DebugConfig {


    public static final boolean isTest = false;
    public static final boolean isDev = false;
    public static final boolean isOnline = true;


    public static boolean isDebug;//true=测试，开发环境 .false=正式环境

    static {
        if (isTest || isDev) {
            isDebug = true;
        } else {
            isDebug = false;
        }

        //手动配置环境,方便调试，默认是没有的
        if (DevSettingSharePref.getInstance().getNeedLog()) {
            isDebug = true;
        }
    }

//    private static String DOMAIN_DEV = "http://devdfshop.dafycredit.cn:9000/";//开发域名
    private static String DOMAIN_DEV = "http://10.10.11.140:9000/";//开发域名
//    private static String DOMAIN_TEST = " http://dafyshop.dafysz.cn:10001/";//测试域名
    private static String DOMAIN_TEST = " http://testdfshop.dafycredit.cn:9000/";//测试域名(唐聪兴)
    private static String DOMAIN_ONLINE = "http://dafyshop02.dafysz.cn/";//正式域名 9月18日修改正式域名


    public static final String API_VERSION = "v1/";//api版本，开发，正式环境可用
    //普通接口baseurl
    public static String BASE_URL_TEST = DOMAIN_TEST + API_VERSION;//测试环境
    public static String BASE_URL_ONLINE = DOMAIN_ONLINE + API_VERSION;//正式环境
    public static String BASE_URL_DEV = DOMAIN_DEV + API_VERSION;//开发环境

    //反馈接口baseurl
    public static String FEED_BACK_ONLINE = "https://wx.dafysz.cn/wechat-web/";
    public static String FEED_BACK_TEST = "http://wx.dafycredit.cn/wechat-web/";

    //h5 baseurl
    public static String H5_ONLINE = "http://3c.dafysz.cn/";
    public static String H5_TEST = "http://wx.dafycredit.cn/";

    //优惠券 baseurl
    public static String WECHAT_ONLINE = "http://wx.dafysz.cn/";
    public static String WECHAT_DEV = "http://idcwxtest.dafysz.cn/";
    public static String WECHAT_TEST = "http://wx.dafycredit.cn/";

    /**
     * 反馈的域名
     *
     * @return
     */
    public static String getFeedBackBaseUrl() {
        if (isOnline) {
            return FEED_BACK_ONLINE;
        } else {
            return FEED_BACK_TEST;
        }
    }

    public static String getCommonQuestionBaseUrl() {
        if (isOnline) {
            return "http://wx.dafysz.cn/";
        } else {
            return "http://wx.dafycredit.cn/";
        }
    }

    public static String getBaseUrl() {
        //手动配置环境,方便调试，默认是没有的
        String debugBaseUrl = DevSettingSharePref.getInstance().getDebugBaseUrl();
        if (!TextUtils.isEmpty(debugBaseUrl)) {
            return debugBaseUrl;
        }

        if (isOnline) {
            return BASE_URL_ONLINE;
        } else if (isDev) {
            return BASE_URL_DEV;
        } else {
            return BASE_URL_TEST;
        }
    }

    public static String getH5BaseUrl() {
        if (isOnline) {
            return H5_ONLINE;
        } else {
            return H5_TEST;
        }
    }

    /**
     * 领取优惠券
     *
     * @return
     */
    public static String getCourtesyUrl() {
        if (isOnline) {
            return WECHAT_ONLINE;
        } else if (isDev) {
            return WECHAT_DEV;
        } else {
            return WECHAT_TEST;
        }
    }
}

