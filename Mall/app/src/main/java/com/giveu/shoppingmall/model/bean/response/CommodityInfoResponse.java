package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/9/8.
 */

public class CommodityInfoResponse extends BaseBean<CommodityInfoResponse> {

    /**
     * freeInterest : 0
     * isPromotion : 1
     * monthAmount : 105.00
     * name : 荣耀 畅玩6X 3GB 32GB 全网通4G手机 标准版 铂光金
     * salePrice : 1,099.00
     * serviceSafeguards : [{}]
     * skuCode : K00002855
     * src : group1/M07/E2/73/CgsLxFmXo5mAYgOfAAT8G3zKiLU385.jpg
     * srcIp : http://fastdfs.dafysz.cn
     * stockStatus : 0
     */

    public int freeInterest;
    public int isPromotion;
    public String monthAmount;
    public String name;
    public String salePrice;
    public String skuCode;
    public String src;
    public String srcIp;
    public int stockStatus;
    public List<ServiceSafeguardsBean> serviceSafeguards;

    public static class ServiceSafeguardsBean {
    }
}
