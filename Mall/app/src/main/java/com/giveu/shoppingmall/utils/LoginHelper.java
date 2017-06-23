package com.giveu.shoppingmall.utils;

import com.giveu.shoppingmall.utils.sharePref.AbsSharePref;

/**
 * 保存用户信息
 */
public class LoginHelper extends AbsSharePref {

    private static LoginHelper instance;

    private final String spName = "login_config";

    public static LoginHelper getInstance() {
        if (instance == null) {
            instance = new LoginHelper();
        }
        return instance;
    }

    private LoginHelper() {
    }

    //退出登录，清空信息
    public void logout() {
        clear();
    }


    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    @Override
    public String getUserId() {
        return null;
    }

    public boolean hasLogin() {
        return false;
    }
    public boolean hasUploadDeviceNumber() {
        return false;
    }



}