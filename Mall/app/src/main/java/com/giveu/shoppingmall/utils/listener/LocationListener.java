package com.giveu.shoppingmall.utils.listener;

import com.amap.api.location.AMapLocation;

/**
 * 定位方法内的监听
 * Created by 508632 on 2017/2/4.
 */

public interface LocationListener {
	void onSuccess(AMapLocation amapLocation_input);
	void onFail(Object o);
}
