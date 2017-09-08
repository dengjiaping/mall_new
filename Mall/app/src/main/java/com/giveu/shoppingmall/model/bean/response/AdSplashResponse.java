package com.giveu.shoppingmall.model.bean.response;

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

    public int adId;
    public boolean enable;
    public String imgUrl;
    public String imgUrlLink;
    public int second;

    public AdSplashResponse(String imgUrl, String imgUrlLink,int second) {
        this.imgUrl = imgUrl;
        this.imgUrlLink = imgUrlLink;
        this.second = second;
    }
}
