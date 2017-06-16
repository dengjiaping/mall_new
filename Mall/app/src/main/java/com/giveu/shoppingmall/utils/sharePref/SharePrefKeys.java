package com.giveu.shoppingmall.utils.sharePref;

import com.giveu.shoppingmall.utils.CommonUtils;

/**
 * Created by zhengfeilong on 16/5/6.
 * 该类的作用：存放与SharedPreferences有关的所有key
 * 命名规范：键的名字大写，单词之间用“_”分隔，值与键保持一样，如有必要请添加注释 如：    String NEW_VERSION_CODE = "NEW_VERSION_CODE";//最新版本号
 */
public interface SharePrefKeys {

    String NEW_VERSION_CODE = "NEW_VERSION_CODE";//最新版本号
    String SERVER_TOKEN = "SERVER_TOKEN";

    /**
     * 设备唯一编号
     */
    String UUID = "UUID";
    String SP_WELCOM = "SP_WELCOM_" + CommonUtils.getVersionCode();
    String AD_SPLASH_IMAGE = "AD_SPLASH_IMAGE";
    String DOWNLOAD_APK_FLAG = "DOWNLOAD_APK_FLAG" + CommonUtils.getVersionCode();
    String LAST_APK_UPDATE_DIALOG_SHOW_TIME = "LAST_APK_UPDATE_DIALOG_SHOW_TIME" + CommonUtils.getVersionCode();
    String LOCK_PATTERN_PWD = "LOCK_PATTERN_PWD";
    String FINGER_PWD = "FINGER_PWD";


}
