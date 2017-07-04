package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/6/30.
 */

public class BankCardListResponse extends BaseBean<BankCardListResponse> {


    public List<BankInfoListBean> bankInfoList;

    public static class BankInfoListBean {
        /**
         * bankNo : 6212264000067716551
         * bankName : 中国工商银行
         * bankCode : 1
         * bankBindPhone : 13026602991
         * bankPerson : 潘梦娟
         * idPerson : 10103773
         * isDefault : 0
         * id : 7191
         * payType : 1
         */

        public String bankNo;
        public String bankName;
        public String bankCode;
        public String bankBindPhone;
        public String bankPerson;
        public int idPerson;
        public int isDefault;
        public int id;
        public String payType;
    }
}
