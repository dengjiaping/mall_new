package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/3.
 */

public class ContractResponse extends BaseBean<ContractResponse> {

    /**
     * dataCount : 76007
     * list : {"contractNo":"测试内容5m6l","creditAmount":66232,"creditStatus":"测试内容l1ip","creditType":"测试内容i2jt","currentInstalment":63881,"idCredit":"测试内容4v16","loanDate":"测试内容e1d4","paymentNum":45488}
     * page : 13210
     * pageCount : 60222
     * pageSize : 58444
     */

    public int dataCount;
    public ArrayList<Contract> list;
    public int page;
    public int pageCount;
    public int pageSize;

    public static class Contract {
        /**
         * contractNo : 测试内容5m6l
         * creditAmount : 66232
         * creditStatus : 测试内容l1ip
         * creditType : 测试内容i2jt
         * currentInstalment : 63881
         * idCredit : 测试内容4v16
         * loanDate : 测试内容e1d4
         * paymentNum : 45488
         */

        public String contractNo;
        public int creditAmount;
        public String creditStatus;
        public String creditType;
        public int currentInstalment;
        public String idCredit;
        public String loanDate;
        public String paymentNum;
    }
}
