package com.giveu.shoppingmall.model.bean.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 101900 on 2017/1/13.
 */

public class BankListBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * bigIco : 测试内容e295
         * smallIco : 测试内容61y1
         * code : 2
         * val : 中国建设银行1
         */

        public String bigIco;
        public String smallIco;
        @SerializedName("code")
        public String codeX;
        public String val;
    }
}
