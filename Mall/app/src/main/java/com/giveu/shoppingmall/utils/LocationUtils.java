package com.giveu.shoppingmall.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.giveu.shoppingmall.utils.listener.LocationListener;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by 101900 on 2017/2/10.
 */

public class LocationUtils implements AMapLocationListener {

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    LocationListener listener;

    public LocationUtils(Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置只定位一次
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        mLocationOption.setLocationCacheEnable(false);
        //设置定位间隔,单位毫秒,5分钟定位一次
     //   mLocationOption.setInterval(300000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        //如果已经开始定位了，应先停止定位
        stopLocation();
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    public void setOnLocationListener(LocationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                if (listener != null) {
                    listener.onSuccess(amapLocation);
                    stopLocation();
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtils.e("location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                if (listener != null) {
                    listener.onFail(amapLocation);
                }
            }
        }
    }

    /**
     * 不需要定位时需销毁
     */
    public void destory() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        listener = null;
    }
}
