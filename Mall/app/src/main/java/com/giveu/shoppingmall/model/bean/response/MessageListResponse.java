package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 101912 on 2017/10/16.
 */

public class MessageListResponse extends BaseBean<MessageListResponse> {

    /**
     * pageNum : 1
     * pageSize : 10
     * pages : 10
     * resultList : [{"imgUrl":"http://img.dfshop.cn/vivi.png","msgNo":"23232323","receiveTime":"2018-09-09 12:25:36","redirectUrl":"http://www.test.com","title":"订单成功","type":75064,"content":"【广州】您的订单已经进入广州10号库准备出库，不能修改"}]
     */

    public int pageNum;
    public int pageSize;
    public int pages;
    public List<ResultListBean> resultList;

    public static class ResultListBean {
        /**
         * imgUrl : http://img.dfshop.cn/vivi.png
         * msgNo : 23232323
         * receiveTime : 2018-09-09 12:25:36
         * redirectUrl : http://www.test.com
         * title : 订单成功
         * type : 75064
         * content : 【广州】您的订单已经进入广州10号库准备出库，不能修改
         */

        public String imgUrl;
        public String msgNo;
        public String receiveTime;
        public String redirectUrl;
        public String title;
        public int type;
        public String content;
    }
}
