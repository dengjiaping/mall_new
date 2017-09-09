package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/6/29.
 */

public class LoginResponse extends BaseBean<LoginResponse> {
    /**
     * idPerson : 14563859
     * hidden : true
     * userPic :
     * endDate : 2067/8/7
     * cyLimit : 50000
     * ident : 370724199806296925
     * repayAmount : 289
     * creditCount : 1
     * bankName : 中国建设银行
     * existOther : 2
     * remainDays : 0
     * bankIconUrl : http://10.10.11.140:9000/img/bankicon/2.png
     * availablePosLimit : 48799.99
     * userLevel : 1
     * hasDefaultCard : true
     * activeDate : 2017/8/7
     * bankNo : 6217002340015437975
     * availableRechargeLimit : 500
     * defaultCard : 6217002340015437975
     * isDk : 1
     * address : {"province":"山东","city":"济南","provinceCode":"","cityCode":""}
     * nickName :
     * isSetPwd : true
     * availableCyLimit : 50000
     * userName : 17688933779
     * accessToken : e2233c1773ea859231d66697bfc9b9c4
     * userId : 80
     * realName : 侯芳
     * globleLimit : 100000
     * posLimit : 50000
     * phone : 17688933779
     * name : 侯芳
     * repayDate : 2017/08/09
     * existLive : 1
     * totalCost : 98799.99
     * status : true
     */

    public String accessToken;
    public String activeDate;
    public String availableCyLimit;
    public String availablePosLimit;
    public String availableRechargeLimit;
    public String creditCount;
    public String cyLimit;
    public String endDate;
    public String globleLimit;
    public String idPerson;
    public String ident;
    public String name;
    public String nickName;
    public String bankName;
    public String bankIconUrl;
    public String defaultCard;
    public String phone;
    public String posLimit;
    public String repayAmount;
    public String repayDate;
    public boolean status;
    public String userId;
    public String userName;
    public String userPic;
    public String totalCost;
    public boolean hasDefaultCard;
    public boolean isSetPwd;
    public String remainDays;
    public String existOther;
    public String existLive;
    public String receivePhone;
    public String receiveName;
    public String receiveProvince;
    public String receiveCity;
    public String receiveRegion;
    public String receiveStreet;
    public String receiveDetailAddress;
    public String orderPayNum;
    public String orderDowmpaymentNum;
    public String orderReceiveNum;


    public AddressBean address;
    public ShoppingAddress shippingAddress;
    public List<MyOrderBean> myOrder;


    public static class AddressBean {
        /**
         * province : 山东
         * city : 济南
         * provinceCode :
         * cityCode :
         */

        public String province;
        public String city;
        public String provinceCode;
        public String cityCode;
    }

    public static class ShoppingAddress{
            public String address;
            public int addressType;
            public String city;
            public int cityCode;
            public String custName;
            public int idPerson;
            public String phone;
            public String province;
            public int provinceCode;
            public String region;
            public int regionCode;
            public String street;
            public int streetCode;
    }

    public static class MyOrderBean {
        /**
         * num : 38167
         * status : 55818
         */

        public String num;
        public int status;
    }
}
