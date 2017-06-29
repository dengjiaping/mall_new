package com.giveu.shoppingmall.utils;

import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.sharePref.AbsSharePref;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

/**
 * 保存用户信息
 */
public class LoginHelper extends AbsSharePref {

    private static LoginHelper instance;
    public LoginResponse loginPersonInfo;
    private final String spName = "login_config";

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String ACTIVE_DATE = "activeDate";
    public static final String AVAILABLE_CYLIMIT = "availableCyLimit";
    public static final String AVAILABLE_POSLIMIT = "availablePosLimit";
    public static final String CERTNO = "certNo";
    public static final String CYLIMIT = "cyLimit";
    public static final String END_DATE = "endDate";
    public static final String GLOBLE_LIMIT = "globleLimit";
    public static final String ID_PERSON = "idPerson";
    public static final String MOBILE = "mobile";
    public static final String NICK_NAME = "nickName";
    public static final String POS_LIMIT = "posLimit";
    public static final String REAL_NAME = "realName";
    public static final String STATUS = "status";
    public static final String STATUS_DESC = "statusDesc";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_PIC = "userPic";

    public static LoginHelper getInstance() {
        if (instance == null) {
            instance = new LoginHelper();
        }
        return instance;
    }

    private LoginHelper() {
        getLoginStatus();
    }

    //查询用户登录信息
    private void getLoginStatus() {
        LoginResponse personInfo = new LoginResponse();
        personInfo.userId = getString(USER_ID, "");
        personInfo.status = getString(STATUS, "1");
        personInfo.userName = getString(USER_NAME, "");
        personInfo.userPic = getString(USER_PIC, "");
        personInfo.globleLimit = getString(GLOBLE_LIMIT, "");
        this.loginPersonInfo = personInfo;
        if (StringUtils.isNotNull(getString(USER_ID))) {
            this.loginPersonInfo = personInfo;
        } else {
            loginPersonInfo = null;
        }
    }

    //保存用户登录信息
    public void saveLoginStatus(LoginResponse personInfo) {
        SharePrefUtil.setAppToken(personInfo.accessToken);
        putString(ACCESS_TOKEN, personInfo.accessToken);
        putString(ACTIVE_DATE, personInfo.activeDate);
        putString(AVAILABLE_CYLIMIT, personInfo.availableCyLimit);
        putString(AVAILABLE_POSLIMIT, personInfo.availablePosLimit);
        putString(CERTNO, personInfo.certNo);
        putString(CYLIMIT, personInfo.cyLimit);
        putString(END_DATE, personInfo.endDate);
        putString(GLOBLE_LIMIT, personInfo.globleLimit);
        putString(ID_PERSON, personInfo.idPerson);
        putString(MOBILE, personInfo.mobile);
        putString(NICK_NAME, personInfo.nickName);
        putString(POS_LIMIT, personInfo.posLimit);
        putString(REAL_NAME, personInfo.realName);
        putString(STATUS, personInfo.status);
        putString(STATUS_DESC, personInfo.statusDesc);
        putString(USER_ID, personInfo.userId);
        putString(USER_NAME, personInfo.userName);
        putString(USER_PIC, personInfo.userPic);
        this.loginPersonInfo = personInfo;
    }

    //退出登录，清空信息
    public void logout() {
        clear();
        loginPersonInfo = null;
    }


    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    public String getStatus() {
        return loginPersonInfo == null ? "1" : loginPersonInfo.status;
    }

    public String getUserName() {
        return loginPersonInfo == null ? null : loginPersonInfo.userName;
    }

    public String getUserPic() {
        return loginPersonInfo == null ? null : loginPersonInfo.userPic;
    }

    public String getGlobleLimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.globleLimit;
    }

    @Override
    public String getUserId() {
        return null;
    }

    public boolean hasLogin() {
        return loginPersonInfo == null ? false : true;
    }

    public boolean hasUploadDeviceNumber() {
        return false;
    }
}