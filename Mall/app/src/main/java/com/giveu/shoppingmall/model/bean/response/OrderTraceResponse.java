package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101912 on 2017/9/4.
 */

public class OrderTraceResponse extends BaseBean<OrderTraceResponse>{


    /**
     * logisticsInfo : [{"content":"【广州】您的订单已经进入广州10号库准备出库，不能修改","msgTime":"2013-09-27 09:04:22","operator":"管理员"},{"content":"您的订单已经进入北京1号库准备出库，不能修改","msgTime":"2013-09-25 09:04:22","operator":"快递员"}]
     * orderNo : JD0000002
     * src : https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg
     * status : 1
     */

    public String orderNo;
    public String src;
    public int status;
    public List<LogisticsInfoBean> logisticsInfo;


    public static class LogisticsInfoBean {
        /**
         * content : 【广州】您的订单已经进入广州10号库准备出库，不能修改
         * msgTime : 2013-09-27 09:04:22
         * operator : 管理员
         */

        public String content;
        public String msgTime;
        public String operator;

    }
}
