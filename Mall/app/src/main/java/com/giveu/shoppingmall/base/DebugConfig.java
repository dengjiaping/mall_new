package com.giveu.shoppingmall.base;


import android.text.TextUtils;

import com.giveu.shoppingmall.utils.sharePref.DevSettingSharePref;

/**
 * 这是调试变量，你可以修改
 */
public class DebugConfig {

    public static final boolean isTest = true;
    public static final boolean isDev = false;
    public static final boolean isOnline = false;


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

    private static String DOMAIN_DEV = "http://10.10.11.140:9000/";//开发域名
    private static String DOMAIN_TEST = "http://testdfshop.dafycredit.cn:9000/";//域名
    private static String DOMAIN_ONLINE = "http://3c.dafysz.cn/";//域名


    public static final String API_VERSION = "v1/";//api版本，开发，正式环境可用
    public static String BASE_URL_TEST = DOMAIN_TEST + API_VERSION;//测试环境
    public static String BASE_URL_ONLINE = DOMAIN_ONLINE + "3c-web/" + API_VERSION;//正式环境
    public static String BASE_URL_DEV = DOMAIN_DEV + API_VERSION;//开发环境

    public static String apk_update_test = DOMAIN_DEV + API_VERSION + "personCenter/account/getVersion";
    public static String apk_update_online = DOMAIN_ONLINE + API_VERSION + "personCenter/account/getVersion";


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
        return "http://3c.dafysz.cn/h5/sales/";
    }

    /**
     * apk升级的接口与其他接口路径不一样，没有项目名字“3c-web/”
     */
    public static String getApkUpdateUrl() {
        if (isTest || isDev) {
            return apk_update_test;
        }
        return apk_update_online;
    }


}

