package com.giveu.shoppingmall.utils;

import android.app.Activity;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.Province;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/9/4.
 * 获取地址工具类
 */

public class AddressUtils {
    public static void getAddressList(final Activity activity, final OnAddressLitener listener) {
        String cacheTime = SharePrefUtil.getInstance().getString(Const.ADDRESS_TIME, "");
        //当本地缓存还是当天缓存的，那么读取本地缓存，出现异常时获取服务器数据
        if (cacheTime.equals(DateUtil.getCurrentTime2yyyyMMdd())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //读取本地的json
                        final String addressJson = StringUtils.loadAddress(BaseApplication.getInstance());
                        Gson gson = new Gson();
                        //解析json
                        final ArrayList<Province> addressList = gson.fromJson(addressJson,
                                new TypeToken<List<Province>>() {
                                }.getType());
                        //在主线程回调
                        if (listener != null && activity != null && !activity.isFinishing()) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(addressList);
                                }
                            });
                        }
                    } catch (Exception e) {
                        //抛出异常后从服务器获取数据
                        if (listener != null && activity != null && !activity.isFinishing()) {
                            getAddListFromInternet(listener);
                        }
                    }
                }
            }).start();
        } else {
            if (activity != null && !activity.isFinishing()) {
                getAddListFromInternet(listener);
            }
        }
    }


    private static void getAddListFromInternet(final OnAddressLitener litener) {
        ApiImpl.getAddListJson(null, new BaseRequestAgent.ResponseListener<Province>() {
            @Override
            public void onSuccess(final Province response) {
                if (CommonUtils.isNotNullOrEmpty(response.data)) {
                    if (litener != null) {
                        litener.onSuccess(response.data);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            //本地缓存地址
                            StringUtils.saveAddress(BaseApplication.getInstance(), gson.toJson(response.data));
                            //缓存地址时间
                            SharePrefUtil.getInstance().putString(Const.ADDRESS_TIME, DateUtil.getCurrentTime2yyyyMMdd());
                        }
                    }).start();

                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (litener != null) {
                    litener.onFail();
                }
            }
        });
    }

    public interface OnAddressLitener {
        void onSuccess(ArrayList<Province> addressList);

        void onFail();
    }
}
