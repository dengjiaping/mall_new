package com.giveu.shoppingmall.utils;

import android.app.Activity;

import com.giveu.shoppingmall.me.view.activity.LoginActivity;
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
    public static final String AVAILABLE_RECHARGE_LIMIT = "availableRechargeLimit";
    public static final String CREDIT_COUNT = "creditCount";
    public static final String REPAY_AMOUNT = "repayAmount";
    public static final String REPAY_DATE = "repayDate";

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
        personInfo.idPerson = getInt(ID_PERSON, 0);
        personInfo.mobile = getString(MOBILE, "");
        personInfo.availableCyLimit = getString(AVAILABLE_CYLIMIT, "");
        personInfo.availablePosLimit = getString(AVAILABLE_POSLIMIT, "");
        personInfo.cyLimit = getString(CYLIMIT, "");
        personInfo.posLimit = getString(POS_LIMIT, "");
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
        putInt(ID_PERSON, personInfo.idPerson);
        putString(MOBILE, personInfo.mobile);
        putString(NICK_NAME, personInfo.nickName);
        putString(POS_LIMIT, personInfo.posLimit);
        putString(REAL_NAME, personInfo.realName);
        putString(STATUS, personInfo.status);
        putString(STATUS_DESC, personInfo.statusDesc);
        putString(USER_ID, personInfo.userId);
        putString(USER_NAME, personInfo.userName);
        putString(USER_PIC, personInfo.userPic);
        putString(AVAILABLE_RECHARGE_LIMIT, personInfo.availableRechargeLimit);
        putString(CREDIT_COUNT, personInfo.creditCount);
        putString(REPAY_AMOUNT, personInfo.repayAmount);
        putString(REPAY_DATE, personInfo.repayDate);
        this.loginPersonInfo = personInfo;
    }

    //退出登录，清空信息
    public void logout() {
        clear();
        loginPersonInfo = null;
    }

    /**
     * 判断是否登录，如果没有跳转至登录界面
     *
     * @param activity
     * @return
     */
    public boolean hasLoginAndGotoLogin(Activity activity) {
        if (loginPersonInfo != null) {
            return true;
        } else {
            LoginActivity.startIt(activity);
            return false;
        }
    }

    /**
     * 是否有钱包资质
     *
     * @return
     */
    public boolean hasQualifications() {
        if ("2".equals(getStatus())) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    /**
     * 1为未激活钱包，2为已激活钱包
     *
     * @return
     */
    public String getStatus() {
        return loginPersonInfo == null ? "1" : loginPersonInfo.status;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getUserName() {
        return loginPersonInfo == null ? null : loginPersonInfo.userName;
    }

    /**
     * 获取用户真实姓名
     * @return
     */
    public String getRealName() {
        return loginPersonInfo == null ? null : loginPersonInfo.realName;
    }

    /**
     * 获取用户头像
     *
     * @return
     */
    public String getUserPic() {
        return loginPersonInfo == null ? null : loginPersonInfo.userPic;
    }

    /**
     * 获取授信总额度
     *
     * @return
     */
    public String getGlobleLimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.globleLimit;
    }

    /**
     * 获取取现总额度
     *
     * @return
     */
    public String getCylimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.cyLimit;
    }

    /**
     * 获取取现可用额度
     *
     * @return
     */
    public String getAvailableCylimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.availableCyLimit;
    }

    /**
     * 获取本期待还金额
     *
     * @return
     */
    public String getRepayAmount() {
        String defalutValue = "0.00";
        if (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.repayAmount)) {
            return defalutValue;
        } else {
            return loginPersonInfo.repayAmount;
        }
    }

    /**
     * 获取消费笔数
     *
     * @return
     */
    public String getCreditCount() {
        String defalutValue = "0";
        if (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.creditCount)) {
            return defalutValue;
        } else {
            return loginPersonInfo.creditCount;
        }
    }

    /**
     * 获取剩余还款日
     *
     * @return
     */
    public String getRepayDate() {
        String defalutValue = "0";
        if (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.repayDate)) {
            return defalutValue;
        } else {
            return loginPersonInfo.repayDate;
        }
    }

    /**
     * 获取可消费额度
     *
     * @return
     */
    public String getAvailablePoslimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.availablePosLimit;
    }

    /**
     * 获取消费总额度
     *
     * @return
     */
    public String getPosLimit() {
        return loginPersonInfo == null ? null : loginPersonInfo.posLimit;
    }

    /**
     * 获取客户Id
     *
     * @return
     */
    public int getIdPerson() {
        return loginPersonInfo == null ? -1 : loginPersonInfo.idPerson;
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getMobile() {
        return loginPersonInfo == null ? null : loginPersonInfo.mobile;
    }

    /**
     * 获取用户Id
     *
     * @return
     */
    public String getUserId() {
        return loginPersonInfo == null ? null : loginPersonInfo.userId;
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean hasLogin() {
        return loginPersonInfo == null ? false : true;
    }

    public boolean hasUploadDeviceNumber() {
        return false;
    }
}