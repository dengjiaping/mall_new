package com.giveu.shoppingmall.model;

import android.app.Activity;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.android.volley.mynet.RequestAgent;
import com.giveu.shoppingmall.base.DebugConfig;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.BankCardListResponse;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.model.bean.response.CheckSmsResponse;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;
import com.giveu.shoppingmall.model.bean.response.CostFeeResponse;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.model.bean.response.ProductResponse;
import com.giveu.shoppingmall.model.bean.response.RandCodeResponse;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;
import com.giveu.shoppingmall.model.bean.response.RepayCostResponse;
import com.giveu.shoppingmall.model.bean.response.RpmDetailResponse;
import com.giveu.shoppingmall.model.bean.response.SmsCodeResponse;
import com.giveu.shoppingmall.model.bean.response.TokenBean;
import com.giveu.shoppingmall.model.bean.response.TransactionDetailResponse;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.model.bean.response.WalletQualifiedResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import java.util.Map;
import java.util.Random;


/**
 * 所有的网络请求放在这个类中
 */


public class ApiImpl {


    //广告页
    public static void AdSplashImage(Activity context, BaseRequestAgent.ResponseListener<AdSplashResponse> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.activity_getImageInfo, AdSplashResponse.class, null, responseListener);
    }

    public static void getAppToken(Activity context, final BaseRequestAgent.ResponseListener<TokenBean> responseListener) {
//        tokenType	带权限token 1 不带权限token 2
        String tokenType = "2";
//        if (LoginHelper.getInstance().hasLogin()){
//            tokenType = "1";
//        }
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "tokenType"}, new String[]{SharePrefUtil.getUUId(), tokenType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.token_getToken, TokenBean.class, context, new BaseRequestAgent.ResponseListener<TokenBean>() {
            @Override
            public void onSuccess(TokenBean response) {
                try {
                    //把token存起来
                    SharePrefUtil.setAppToken(response.data.accessToken);

                    if (responseListener != null) {
                        responseListener.onSuccess(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                try {
                    if (responseListener != null) {
                        responseListener.onError(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void sendApkUpgradeRequest(Activity context, BaseRequestAgent.ResponseListener<ApkUgradeResponse> responseListener) {
        double d = new Random().nextDouble();//加一个随机数，避免接口缓存
        Map<String, String> requestParams2 = BaseRequestAgent.getRequestParams(new String[]{"version", "t"},
                new String[]{CommonUtils.getVersionCode(), String.valueOf(d)});

        RequestAgent.getInstance().sendGetRequest(requestParams2, DebugConfig.getApkUpdateUrl(), ApkUgradeResponse.class, context, responseListener);
    }


    //忘记手势密码调用这个验证文本密码
    public static void checkPwd(Activity context, String password, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bind", "identification", "password"}, new String[]{"2", SharePrefUtil.getUUId(), password});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.sales_account_checkPwd, BaseBean.class, context, responseListener);
    }

    //保存极光设备号
    public static void saveDeviceNumber(String deviceNumber) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{ApiUrl.COMMON_KEY, "deviceNumber"}, new String[]{ApiUrl.api_Account_SaveDeviceNumber, deviceNumber});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.common_url, BaseBean.class, null, new BaseRequestAgent.ResponseListener() {
            @Override
            public void onSuccess(BaseBean response) {
                //设置为已上传过设备号
//                LoginHelper.getInstance().setHasUploadDeviceNumber(true);
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }

    //获取用户信息
    public static void getUserInfo(Activity context, String idPerson, String userId, BaseRequestAgent.ResponseListener<LoginResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson","userId"}, new Object[]{Long.parseLong(idPerson),userId});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getUserInfo, LoginResponse.class, context, responseListener);
    }

    //用户注册
    public static void register(Activity context, String mobile, String password, String smsCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "mobile", "password", "smsCode"}, new String[]{SharePrefUtil.getUUId(), mobile, password, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_register, RegisterResponse.class, context, responseListener);
    }

    //下发短信验证码
    public static void sendSMSCode(Activity context, String phone, String codeType, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"phone", "codeType",}, new String[]{phone, codeType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_util_sendSMSCode, BaseBean.class, context, responseListener);
    }

    //校验短信验证码
    public static void chkValiCode(Activity context, String code, String phone, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"code", "phone", "codeType"}, new String[]{code, phone, "regType"});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_util_chkValiCode, BaseBean.class, context, responseListener);
    }

    //用户注册
    public static void login(Activity context, String userName, String password, BaseRequestAgent.ResponseListener<LoginResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "userName", "password"}, new String[]{SharePrefUtil.getUUId(), userName, password});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_login, LoginResponse.class, context, responseListener);
    }

    //钱包激活发送短信接口
    public static void sendActivateSmsCode(Activity context, String bankNo, int idPerson, String ident, String name, String phone, BaseRequestAgent.ResponseListener<SmsCodeResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankNo", "idPerson", "ident", "name", "phone"}, new Object[]{bankNo, idPerson, ident, name, phone});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_sendActivateSmsCode, SmsCodeResponse.class, context, responseListener);
    }

    //钱包资质判定
    public static void getWalletQualified(Activity context, String ident, String name, BaseRequestAgent.ResponseListener<WalletQualifiedResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"ident", "name"}, new Object[]{ident, name});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getWalletQualified, WalletQualifiedResponse.class, context, responseListener);
    }

    //钱包激活
    public static void activateWallet(Activity context, String bankNo, int idPerson, String ident, String latitude, String longitude,String orderNo, String phone, String realName,String sendSource, String smsCode,String smsSeq, BaseRequestAgent.ResponseListener<WalletActivationResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankNo", "idPerson", "ident", "latitude", "longitude","orderNo", "phone", "realName","sendSource", "smsCode", "smsSeq","userId"}, new Object[]{bankNo, idPerson, ident, latitude, longitude,orderNo, phone, realName,sendSource, smsCode, smsSeq, Integer.parseInt(LoginHelper.getInstance().getUserId())});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_activateWallet, WalletActivationResponse.class, context, responseListener);
    }


    //找回登录密码（校验短信码）
    public static void checkSmsCode(Activity context, String mobile, String smsCode, BaseRequestAgent.ResponseListener<CheckSmsResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "smsCode"}, new String[]{mobile, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkSmsCode, CheckSmsResponse.class, context, responseListener);
    }

    //找回密码（重置密码）
    public static void resetPassword(Activity context, String mobile, String newPwd, String userName, String randCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "newPwd", "userName", "randCode"}, new String[]{mobile, newPwd, userName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetLoginPwd, RegisterResponse.class, context, responseListener);
    }

    //找回密码（校验身份）
    public static void checkUserInfo(Activity context, String certNo, String mobile, String randCode, String realName, BaseRequestAgent.ResponseListener<RandCodeResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"certNo", "mobile", "realName", "randCode"}, new String[]{certNo, mobile, realName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkUserInfo, RandCodeResponse.class, context, responseListener);
    }

    //设置交易密码
    public static void setPayPwd(Activity context, String confirmPwd, int idPerson, String newPwd, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"confirmPwd", "idPerson", "newPwd"}, new Object[]{confirmPwd, idPerson, newPwd});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_setPayPwd, BaseBean.class, context, responseListener);
    }
    //找回交易密码（重置密码）
    public static void resetPayPwd(Activity context, String confirmPwd, int idPerson, String newPwd,String phone,String smsCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"confirmPwd", "idPerson", "newPwd","phone", "smsCode"}, new Object[]{confirmPwd, idPerson, newPwd,phone, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPayPwd, BaseBean.class, context, responseListener);
    }


    //校验交易密码
    public static void verifyPayPwd(Activity context, int idPerson, String tradPwd, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "tradPwd"}, new Object[]{idPerson, tradPwd});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_verifyPayPwd, BaseBean.class, context, responseListener);
    }

    //修改手机号
    public static void updatePhone(Activity context, int idPerson, String phone, String randCode, String smsCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "phone", "randCode", "smsCode"}, new Object[]{idPerson, phone, randCode, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_updatePhone, BaseBean.class, context, responseListener);
    }

    //修改手机号
    public static void getBankCardInfo(Activity context, int idPerson, BaseRequestAgent.ResponseListener<BankCardListResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{idPerson});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_getBankInfo, BankCardListResponse.class, context, responseListener);
    }

    //还款首页
    public static void getBillList(Activity context, String idPerson, BaseRequestAgent.ResponseListener<BillListResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{Long.parseLong(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getRepaymentInfo, BillListResponse.class, context, responseListener);
    }

    //还款明细
    public static void getInstalmentDetails(Activity context, String idCredit, boolean isCurrent, String numInstalment, String productType, BaseRequestAgent.ResponseListener<InstalmentDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit", "isCurrent", "numInstalment", "productType"}, new Object[]{idCredit, isCurrent, numInstalment == null ? null : Integer.parseInt(numInstalment), productType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getInstalmentDetails, InstalmentDetailResponse.class, context, responseListener);
    }

    //交易查询
    public static void searchContract(Activity context, String creditStatus, String creditType, String idPerson, String loanDate, int page, int pageSize, String timeType, BaseRequestAgent.ResponseListener<ContractResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"creditStatus", "creditType", "idPerson", "loanDate", "page", "pageSize", "timeType"}, new Object[]{creditStatus, creditType, idPerson, loanDate, page, pageSize, timeType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_listContract, ContractResponse.class, context, responseListener);
    }

    //交易详细
    public static void getContractDetails(Activity context, String idCredit, String creditType, BaseRequestAgent.ResponseListener<TransactionDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit", "creditType"}, new Object[]{idCredit, creditType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getContractDetails, TransactionDetailResponse.class, context, responseListener);
    }

    //分期明细
    public static void getListInstalment(Activity context, String idCredit, BaseRequestAgent.ResponseListener<ListInstalmentResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit"}, new Object[]{idCredit});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_listInstalment, ListInstalmentResponse.class, context, responseListener);
    }

    //删除银行卡
    public static void deleteBankInfo(Activity context, String id, int idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"id", "idPerson"}, new Object[]{id, idPerson});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_deleteBankInfo, BaseBean.class, context, responseListener);
    }

    //设置默认代扣卡
    public static void setDefaultCard(Activity context, String id, int idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"id", "idPerson"}, new Object[]{id, idPerson});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_setDefaultCard, BaseBean.class, context, responseListener);
    }
    //初始化金融产品接口
    public static void initProduct(Activity context,String availableCyLimit, BaseRequestAgent.ResponseListener<ProductResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"availableCyLimit"}, new Object[]{StringUtils.string2Double(availableCyLimit)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_getProducts, ProductResponse.class, context, responseListener);
    }

    //分期产品取现费用计算
    public static void repayCost(Activity context, int idProduct, int loan, BaseRequestAgent.ResponseListener<RepayCostResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idProduct", "loan"}, new Object[]{idProduct, loan});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_repaycost, RepayCostResponse.class, context, responseListener);
    }

    //随借随还取现费用计算
    public static void getCostFee(Activity context, BaseRequestAgent.ResponseListener<CostFeeResponse> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.personCenter_enchashment_enchashment, CostFeeResponse.class, context, responseListener);
    }
    //分期取现月供明细
    public static void rpmDetail(Activity context,int idProduct, int loan, BaseRequestAgent.ResponseListener<RpmDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idProduct", "loan"}, new Object[]{idProduct, loan});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_rpmDetail, RpmDetailResponse.class, context, responseListener);
    }
}





