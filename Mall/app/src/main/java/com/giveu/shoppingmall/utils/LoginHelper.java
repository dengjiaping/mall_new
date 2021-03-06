package com.giveu.shoppingmall.utils;

import android.app.Activity;

import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.LoginActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.sharePref.AbsSharePref;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import java.util.HashMap;

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
    public static final String PHONE = "phone";
    public static final String NICK_NAME = "nickName";
    public static final String POS_LIMIT = "posLimit";
    public static final String NAME = "NAME";
    public static final String STATUS = "status";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_PIC = "userPic";
    public static final String AVAILABLE_RECHARGE_LIMIT = "availableRechargeLimit";
    public static final String CREDIT_COUNT = "creditCount";
    public static final String REPAY_AMOUNT = "repayAmount";
    public static final String REPAY_DATE = "repayDate";
    public static final String TOTAL_COST = "totalCost";
    public static final String HAS_DEFAULT_CARD = "hasDefaultCard";
    public static final String REMAINING_TIMES = "remainingTimes";//手势或指纹剩余提醒次数
    public static final String BANK_NAME = "bankName";
    public static final String BANK_ICON_URL = "bankIconUrl";
    public static final String DEFAULT_CARD = "defaultCard";
    public static final String ISSETPWD = "isSetPwd";
    public static final String REMEBER_ACCOUNT = "rememberAccount";
    public static final String REMAIN_DAYS = "remainDays";
    public static final String EXISTOTHER = "existOther";
    public static final String EXISTLIVE = "existLive";
    public static final String RECEIVE_PHONE = "receivePhone";
    public static final String RECEIVE_NAME = "receiveName";
    public static final String RECEIVE_PROVINCE = "receiveProvince";
    public static final String RECEIVE_CITY = "receiveCity";
    public static final String RECEIVE_REGION = "receiveRegion";
    public static final String RECEIVE_STREET = "receiveStreet";
    public static final String RECEIVE_DETAIL_ADDRESS = "receiveDetailAddress";
    public static final String ORDER_PAY_NUM = "orderPayNum";
    public static final String ORDER_DOWN_PAYMENT = "orderDownPayment";
    public static final String ORDER_RECEIVE_NUM = "orderReceiceNum";
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗
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
        personInfo.status = getBoolean(STATUS, false);
        personInfo.userName = getString(USER_NAME, "");
        personInfo.userPic = getString(USER_PIC, "");
        personInfo.globleLimit = getString(GLOBLE_LIMIT, "");
        personInfo.idPerson = getString(ID_PERSON, "");
        personInfo.phone = getString(PHONE, "");
        personInfo.availableCyLimit = getString(AVAILABLE_CYLIMIT, "0");
        personInfo.availablePosLimit = getString(AVAILABLE_POSLIMIT, "0");
        personInfo.cyLimit = getString(CYLIMIT, "0");
        personInfo.posLimit = getString(POS_LIMIT, "0");
        personInfo.name = getString(NAME, "");
        personInfo.hasDefaultCard = getBoolean(HAS_DEFAULT_CARD, false);
        personInfo.availableRechargeLimit = getString(AVAILABLE_RECHARGE_LIMIT, "0");
        personInfo.totalCost = getString(TOTAL_COST, "");
        personInfo.ident = getString(CERTNO, "");
        personInfo.bankName = getString(BANK_NAME, "");
        personInfo.bankIconUrl = getString(BANK_ICON_URL, "");
        personInfo.defaultCard = getString(DEFAULT_CARD, "");
        personInfo.isSetPwd = getBoolean(ISSETPWD, false);
        personInfo.remainDays = getString(REMAIN_DAYS, "0");
        personInfo.existOther = getString(EXISTOTHER, "0");
        personInfo.existLive = getString(EXISTLIVE, "0");
        personInfo.creditCount = getString(CREDIT_COUNT, "0");
        personInfo.repayAmount = getString(REPAY_AMOUNT, "0");
        personInfo.receiveName = getString(RECEIVE_NAME, "");
        personInfo.receivePhone = getString(RECEIVE_PHONE, "");
        personInfo.receiveProvince = getString(RECEIVE_PROVINCE, "");
        personInfo.receiveCity = getString(RECEIVE_CITY, "");
        personInfo.receiveRegion = getString(RECEIVE_REGION, "");
        personInfo.receiveStreet = getString(RECEIVE_STREET, "");
        personInfo.receiveDetailAddress = getString(RECEIVE_DETAIL_ADDRESS, "");
        personInfo.orderPayNum = getInt(ORDER_PAY_NUM, 0);
        personInfo.orderDownPaymentNum = getInt(ORDER_DOWN_PAYMENT, 0);
        personInfo.orderReceiveNum = getInt(ORDER_RECEIVE_NUM, 0);

        if (StringUtils.isNotNull(getString(USER_ID))) {
            this.loginPersonInfo = personInfo;
        } else {
            loginPersonInfo = null;
        }
    }

    //保存用户登录信息
    public void saveLoginStatus(LoginResponse personInfo, boolean isLogin) {
        SharePrefUtil.setAppToken(personInfo.accessToken);
        putString(ACCESS_TOKEN, personInfo.accessToken);
        putString(ACTIVE_DATE, personInfo.activeDate);
        putString(AVAILABLE_CYLIMIT, personInfo.availableCyLimit);
        putString(AVAILABLE_POSLIMIT, personInfo.availablePosLimit);
        putString(CERTNO, personInfo.ident);
        putString(CYLIMIT, personInfo.cyLimit);
        putString(END_DATE, personInfo.endDate);
        //只有登录的时候才保存userId，查询个人信息不保存，防止后台查询个人信息时返回userId为空
        if (isLogin) {
            putString(USER_ID, personInfo.userId);
        }
        putString(GLOBLE_LIMIT, personInfo.globleLimit);
        putString(ID_PERSON, personInfo.idPerson);
        putString(PHONE, personInfo.phone);
        putString(NICK_NAME, personInfo.nickName);
        putString(POS_LIMIT, personInfo.posLimit);
        putString(NAME, personInfo.name);
        putBoolean(STATUS, personInfo.status);
        putString(USER_NAME, personInfo.userName);
        putString(USER_PIC, personInfo.userPic);
        putString(AVAILABLE_RECHARGE_LIMIT, personInfo.availableRechargeLimit);
        putString(CREDIT_COUNT, personInfo.creditCount);
        putString(REPAY_AMOUNT, personInfo.repayAmount);
        putString(REPAY_DATE, personInfo.repayDate);
        putBoolean(HAS_DEFAULT_CARD, personInfo.hasDefaultCard);
        putString(TOTAL_COST, personInfo.totalCost);
        putString(BANK_NAME, personInfo.bankName);
        putString(BANK_ICON_URL, personInfo.bankIconUrl);
        putString(DEFAULT_CARD, personInfo.defaultCard);
        putBoolean(ISSETPWD, personInfo.isSetPwd);
        putString(REMEBER_ACCOUNT, personInfo.phone);
        putString(REMAIN_DAYS, personInfo.remainDays);
        putString(EXISTOTHER, personInfo.existOther);
        putString(EXISTLIVE, personInfo.existLive);
        if (personInfo.shippingAddress != null) {
            putString(RECEIVE_NAME, personInfo.shippingAddress.custName);
            putString(RECEIVE_PHONE, personInfo.shippingAddress.phone);
            putString(RECEIVE_PROVINCE, personInfo.shippingAddress.province);
            putString(RECEIVE_CITY, personInfo.shippingAddress.city);
            putString(RECEIVE_REGION, personInfo.shippingAddress.region);
            putString(RECEIVE_STREET, personInfo.shippingAddress.street);
            putString(RECEIVE_DETAIL_ADDRESS, personInfo.shippingAddress.address);
        }

        if (CommonUtils.isNotNullOrEmpty(personInfo.myOrder)) {
            //存放待付款、待首付、待收货的订单数
            HashMap orderNum = new HashMap(personInfo.myOrder.size());
            for (LoginResponse.MyOrderBean myOrderBean : personInfo.myOrder) {
                orderNum.put(myOrderBean.status, myOrderBean.num);
            }
            if (orderNum.get(OrderState.WAITINGPAY) != null) {
                putInt(ORDER_PAY_NUM, (int) orderNum.get(OrderState.WAITINGPAY));
            } else {
                putInt(ORDER_PAY_NUM, 0);
            }
            if (orderNum.get(OrderState.DOWNPAYMENT) != null) {
                putInt(ORDER_DOWN_PAYMENT, (int) orderNum.get(OrderState.DOWNPAYMENT));
            } else {
                putInt(ORDER_DOWN_PAYMENT, 0);
            }
            if (orderNum.get(OrderState.WAITINGRECEIVE) != null) {
                putInt(ORDER_RECEIVE_NUM, (int) orderNum.get(OrderState.WAITINGRECEIVE));
            } else {
                putInt(ORDER_RECEIVE_NUM, 0);
            }
        } else {
            putInt(ORDER_PAY_NUM, 0);
            putInt(ORDER_DOWN_PAYMENT, 0);
            putInt(ORDER_RECEIVE_NUM, 0);
        }
        //剩余提醒次数
        int remainingTimes = getInt(REMAINING_TIMES, -1);
        //如果没存过该值，那么是刚登陆时保存的数据，有两次提醒设置手势或指纹的机会
        if (remainingTimes == -1) {
            putInt(REMAINING_TIMES, 2);
        } else {
            putInt(REMAINING_TIMES, remainingTimes);
        }
        getLoginStatus();
    }

    //退出登录，清空信息
    public void logout() {
        clear();
        //保存登录账号
        if (loginPersonInfo != null) {
            putString(REMEBER_ACCOUNT, loginPersonInfo.phone);
        }
        //退出登录后清空有权限的token
        SharePrefUtil.setAppToken("");
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
     * 是否登录而且激活
     */
    public boolean hasLoginAndActivation(Activity activity) {
        if (hasLoginAndGotoLogin(activity)) {
            if (LoginHelper.getInstance().hasQualifications()) {
                return true;
            } else {
                notActiveDialog = new NotActiveDialog(activity);
                notActiveDialog.showDialog();
            }
        }
        return false;
    }

    public int getOrderPayNum() {
        return loginPersonInfo == null ? 0 : loginPersonInfo.orderPayNum;
    }

    public int getOrderDownPaymentNum() {
        return loginPersonInfo == null ? 0 : loginPersonInfo.orderDownPaymentNum;
    }

    public int getOrderReceiveNum() {
        return loginPersonInfo == null ? 0 : loginPersonInfo.orderReceiveNum;
    }

    /**
     * 最大充值额度
     *
     * @return
     */
    public String getAvailableRechargeLimit() {
        return loginPersonInfo == null ? "0.00" : loginPersonInfo.availableRechargeLimit;
    }

    /**
     * 设置手势或指纹提醒
     *
     * @return
     */
    public boolean shouldShowSetting() {
        return loginPersonInfo != null && getInt(REMAINING_TIMES, 0) >= 1;
    }

    /**
     * 减少设置指纹或手势提醒次数
     */
    public void reduceRemingTimes() {
        putInt(REMAINING_TIMES, getInt(REMAINING_TIMES, 0) - 1);
    }

    /**
     * 是否有钱包资质
     *
     * @return
     */
    public boolean hasQualifications() {
        return loginPersonInfo != null && loginPersonInfo.status;

//        return true;

    }

    /**
     * 默认银行卡名称
     *
     * @return
     */
    public String getBankName() {
        return loginPersonInfo == null ? null : loginPersonInfo.bankName;
    }

    /**
     * 设置默认卡名称
     */
    public void setBankName(String bankName) {
        if (loginPersonInfo != null) {
            loginPersonInfo.bankName = bankName;
            putString(BANK_NAME, loginPersonInfo.bankName);
        }
    }

    /**
     * 收货手机号
     *
     * @return
     */
    public String getReceivePhone() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receivePhone;
    }

    /**
     * 收货人姓名
     *
     * @return
     */
    public String getReceiveName() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveName;

    }

    public String getReceiveProvince() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveProvince;

    }

    public String getReceiveCity() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveCity;

    }

    public String getReceiveRegion() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveRegion;

    }

    public String getReceiveStreet() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveStreet;

    }

    /**
     * 收货人详细地址
     *
     * @return
     */
    public String getReceiveDetailAddress() {
        return loginPersonInfo == null ? "" : loginPersonInfo.receiveDetailAddress;
    }

    /**
     * 记住登录账号
     *
     * @return
     */
    public String getRemeberAccount() {
        return getString(REMEBER_ACCOUNT, "");
    }

    /**
     * 默认银行卡Icon的url
     *
     * @return
     */
    public String getBankIconUrl() {
        return loginPersonInfo == null ? null : loginPersonInfo.bankIconUrl;
    }

    /**
     * 设置默认卡Icon的url
     */
    public void setBankIconUrl(String bankIconUrl) {
        if (loginPersonInfo != null) {
            loginPersonInfo.bankIconUrl = bankIconUrl;
            putString(BANK_ICON_URL, loginPersonInfo.bankIconUrl);
        }
    }

    /**
     * 默认银行卡号
     *
     * @return
     */
    public String getDefaultCard() {
        return loginPersonInfo == null ? null : loginPersonInfo.defaultCard;
    }

    /**
     * 设置默认卡卡号
     */
    public void setDefaultCard(String defaultCard) {
        if (loginPersonInfo != null) {
            loginPersonInfo.defaultCard = defaultCard;
            putString(DEFAULT_CARD, loginPersonInfo.defaultCard);
        }
    }

    /**
     * 还款剩余天数
     *
     * @return
     */
    public String getRemainDays() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.remainDays)) ? "0" : loginPersonInfo.remainDays;
    }

    @Override
    public String getSharedPreferencesName() {
        return spName;
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
     * 可用额度
     *
     * @return
     */
    public String getTotalCost() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.totalCost)) ? "0.00" : loginPersonInfo.totalCost;
    }

    /**
     * 是否有默认银行卡
     *
     * @return
     */
    public boolean hasDefaultCard() {
        return loginPersonInfo != null && loginPersonInfo.hasDefaultCard;
    }

    /**
     * 是否已经设置交易密码
     *
     * @return
     */
    public boolean hasSetPwd() {
        return loginPersonInfo != null && loginPersonInfo.isSetPwd;
    }

    /**
     * 设置是否有交易密码
     *
     * @return
     */
    public void setHasSetPwd(boolean isSetPwd) {
        if (loginPersonInfo != null) {
            loginPersonInfo.isSetPwd = isSetPwd;
            putBoolean(ISSETPWD, loginPersonInfo.isSetPwd);
        }
    }

    /**
     * 是否有居住地址
     *
     * @return
     */
    public boolean hasExistLive() {
        if (loginPersonInfo != null && !"0".equals(loginPersonInfo.existLive)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置是否有居住地址
     *
     * @return
     */
    public void setHasExistLive(String existLive) {
        if (loginPersonInfo != null) {
            loginPersonInfo.existLive = existLive;
            putString(EXISTLIVE, loginPersonInfo.existLive);
        }
    }

    /**
     * 是否已经设置其他联系人 true 有
     *
     * @return
     */
    public boolean hasExistOther() {
        if (loginPersonInfo != null && "1".equals(loginPersonInfo.existOther)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置是否有其他联系人
     *
     * @return
     */
    public void setHasExistOther(String existOther) {
        if (loginPersonInfo != null) {
            loginPersonInfo.existOther = existOther;
            putString(EXISTOTHER, loginPersonInfo.existOther);
        }
    }

    /**
     * 获取用户真实姓名
     *
     * @return
     */
    public String getName() {
        return loginPersonInfo == null ? null : loginPersonInfo.name;
    }

    /**
     * 获取用户身份证
     *
     * @return
     */
    public String getIdent() {
        return loginPersonInfo == null ? null : loginPersonInfo.ident;
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
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.globleLimit)) ? "0.00" : loginPersonInfo.globleLimit;
    }

    /**
     * 获取取现总额度
     *
     * @return
     */
    public String getCylimit() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.cyLimit)) ? "0.00" : loginPersonInfo.cyLimit;
    }

    /**
     * 获取取现可用额度
     *
     * @return
     */
    public String getAvailableCylimit() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.availableCyLimit)) ? "0.00" : loginPersonInfo.availableCyLimit;
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
     * 更新可消费额度
     *
     * @param usehAmount 使用的金额
     */
    public void setAvailablePoslimit(int usehAmount) {
        if (loginPersonInfo != null) {
            loginPersonInfo.availablePosLimit = String.valueOf(Double.parseDouble(loginPersonInfo.availablePosLimit) - usehAmount);
            putString(AVAILABLE_CYLIMIT, loginPersonInfo.availableCyLimit);
        }
    }

    /**
     * 设置是否有默认卡
     */
    public void setHasDefaultCard(boolean isDefalut) {
        if (loginPersonInfo != null) {
            loginPersonInfo.hasDefaultCard = isDefalut;
            putBoolean(HAS_DEFAULT_CARD, loginPersonInfo.hasDefaultCard);
        }
    }

    /**
     * 获取可消费额度
     *
     * @return
     */
    public String getAvailablePoslimit() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.availablePosLimit)) ? "0.00" : loginPersonInfo.availablePosLimit;
    }

    /**
     * 获取消费总额度
     *
     * @return
     */
    public String getPosLimit() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.posLimit)) ? "0.00" : loginPersonInfo.posLimit;
    }

    /**
     * 设置idPerson
     *
     * @param idPerson
     */
    public void setIdPerson(String idPerson) {
        if (loginPersonInfo != null) {
            loginPersonInfo.idPerson = idPerson;
            putString(ID_PERSON, loginPersonInfo.idPerson);
        }


    }

    /**
     * 判断是否是假用户（普通假数据激活用户）123 999999
     *
     * @return
     */
    public boolean hasAverageUser() {
        if (loginPersonInfo != null) {
            String idPerson = loginPersonInfo.idPerson;
            if (idPerson != null && idPerson.length() > 6) {
                String idPersonStr = idPerson.substring(idPerson.length() - 6, idPerson.length());
                if ("999999".equals(idPersonStr)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取客户Id
     *
     * @return
     */
    public String getIdPerson() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.idPerson)) ? "0" : loginPersonInfo.idPerson;
    }


    /**
     * 获取手机号
     *
     * @return
     */
    public String getPhone() {
        return loginPersonInfo == null ? null : loginPersonInfo.phone;
    }

    /**
     * 设置手机号
     *
     * @param phone
     */
    public void setPhone(String phone) {
        if (loginPersonInfo != null) {
            loginPersonInfo.phone = phone;
            putString(PHONE, loginPersonInfo.phone);
        }
    }

    /**
     * 获取用户Id
     *
     * @return
     */
    public String getUserId() {
        return (loginPersonInfo == null || StringUtils.isNull(loginPersonInfo.userId)) ? "0" : loginPersonInfo.userId;
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean hasLogin() {
        return loginPersonInfo != null;
    }

    public boolean hasUploadDeviceNumber() {
        return false;
    }
}