package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101912 on 2017/9/5.
 */

public class OrderNumResponse extends BaseBean<OrderNumResponse>{


    public List<MyOrderBean> myOrder;


    public static class MyOrderBean {
        /**
         * num : 10
         * status : 1
         */

        public int num;
        public int status;

    }
}
