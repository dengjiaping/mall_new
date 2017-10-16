package com.giveu.shoppingmall.model.bean.response;

import android.text.TextUtils;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/1/20.
 */

public class AdSplashResponse extends BaseBean<AdSplashResponse> {
    /**
     * adId : 12
     * enable : true
     * imgUrl : http://img.dfshop.cn/vivi.png
     * imgUrlLink : 测试内容35fa
     * second : 3
     */

    public long adId;
    public boolean enable;
    public String imgUrl;
    public String imgUrlLink;
    public String title;
    public int second;

    public boolean enableAd() {
        if (enable && !TextUtils.isEmpty(imgUrl)){
            return true;
        }
        return false;
    }

}
