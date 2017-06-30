package com.giveu.shoppingmall.model;

import android.app.Activity;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.android.volley.mynet.RequestAgent;
import com.giveu.shoppingmall.base.DebugConfig;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.CheckSmsResponse;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;
import com.giveu.shoppingmall.model.bean.response.TokenBean;
import com.giveu.shoppingmall.utils.CommonUtils;
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

    //用户注册
    public static void register(Activity context, String mobile, String password, String smsCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "mobile", "password", "smsCode"}, new String[]{SharePrefUtil.getUUId(), mobile, password, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_register, RegisterResponse.class, context, responseListener);
    }

    //下发短信验证码
    public static void sendSMSCode(Activity context, String phone, String codeType,BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
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

    //找回登录密码（校验短信码）
    public static void checkSmsCode(Activity context, String mobile, String smsCode, BaseRequestAgent.ResponseListener<CheckSmsResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "smsCode"}, new String[]{mobile, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkSmsCode, CheckSmsResponse.class, context, responseListener);
    }

    //找回密码（重置密码）
    public static void changePassword(Activity context, String mobile, String newPwd, String userName, String randCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "newPwd", "userName", "randCode"}, new String[]{mobile, newPwd, userName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetLoginPwd, RegisterResponse.class, context, responseListener);
    }

    //找回密码（校验身份）
    public static void checkUserInfo(Activity context, String certNo, String mobile, String randCode, String userName, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"certNo", "mobile", "userName", "randCode"}, new String[]{certNo, mobile, userName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkUserInfo, BaseBean.class, context, responseListener);
    }

}




