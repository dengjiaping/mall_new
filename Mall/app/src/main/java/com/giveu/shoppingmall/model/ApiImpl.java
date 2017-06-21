package com.giveu.shoppingmall.model;

import android.app.Activity;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.android.volley.mynet.RequestAgent;
import com.giveu.shoppingmall.base.DebugConfig;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.TokenBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
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














}




