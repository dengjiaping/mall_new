package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;

/**
 * Created by 101912 on 2017/8/17.
 */

public class CouponListResponse extends BaseBean<ArrayList<CouponListResponse>> {


    /**
     * prizeId : 1
     * readFlag : 1
     * beginTime : 1502954131000
     * createTime : 1502956523000
     * descpt : 仅限oppo品牌手机商品
     * endTime : 1502954133000
     * id : 2
     * personId : 123456
     * price : 10
     * status : 1
     * subtitle : 消费满3200可用
     * updateTime : 1502956523000
     * useRule : 适用于商城的借记卡消费
     * useRuleDesc : 优惠券使用时间：2017.9.19-2017.10.31即有商城即将上线，敬请期待
     * userId : 124578
     */

    public int prizeId;
    public int readFlag;
    public long beginTime;
    public long createTime;
    public String descpt;
    public long endTime;
    public String id;
    public String personId;
    public String price;
    public String status;
    public String subtitle;
    public long updateTime;
    public String useRule;
    public String useRuleDesc;
    public String userId;
    public boolean isDivider;

    /**
     * cardType : OPPO
     */

    private String cardType;


}
