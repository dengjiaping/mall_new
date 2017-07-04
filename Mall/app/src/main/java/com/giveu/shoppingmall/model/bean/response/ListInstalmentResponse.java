package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/7/4.
 */

public class ListInstalmentResponse extends BaseBean<ListInstalmentResponse> {

    public List<Instalment> instalmentList;

    public static class Instalment {
        /**
         * amount : 58377
         * dueDate : 测试内容i708
         * num : 85106
         * payStatus : 测试内容5904
         */

        public String amount;
        public String dueDate;
        public int num;
        public String payStatus;
    }
}
