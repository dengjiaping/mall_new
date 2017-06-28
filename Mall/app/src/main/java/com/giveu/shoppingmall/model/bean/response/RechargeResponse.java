package com.giveu.shoppingmall.model.bean.response;


import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/1/20.
 */

public class RechargeResponse extends BaseBean<RechargeResponse> {

    /**
     * calls : {"operatorName":"中国联通","operatorType":"CUCC","products":[{"denomination":"30","productId":"5","productName":"30元","salesPrice":"30"},{"denomination":"50","productId":"6","productName":"50元","salesPrice":"49.98"},{"denomination":"100","productId":"7","productName":"100元","salesPrice":"99.98"}]}
     * flows : {"operatorName":"中国联通","operatorType":"CUCC","products":[{"denomination":"6","productId":"16","productName":"50M","salesPrice":"5.82"},{"denomination":"10","productId":"17","productName":"100M","salesPrice":"9.7"},{"denomination":"15","productId":"18","productName":"200M","salesPrice":"14.55"},{"denomination":"30","productId":"19","productName":"500M","salesPrice":"29.1"}]}
     */

    public ListBean calls;
    public ListBean flows;

    public String phoneArea;
    public static class ListBean {
        /**
         * operatorName : 中国联通
         * operatorType : CUCC
         * products : [{"denomination":"30","productId":"5","productName":"30元","salesPrice":"30"},{"denomination":"50","productId":"6","productName":"50元","salesPrice":"49.98"},{"denomination":"100","productId":"7","productName":"100元","salesPrice":"99.98"}]
         */

        public String operatorName;
        public String operatorType;
        public List<ProductsBean> products;

        public ListBean(String operatorName, String operatorType, List<ProductsBean> products) {
            this.operatorName = operatorName;
            this.operatorType = operatorType;
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * denomination : 30
             * productId : 5
             * productName : 30元
             * salesPrice : 30
             */

            public String denomination;
            public String productId;
            public String productName;
            public String salesPrice;
            public int status = 0;

            public ProductsBean(String productId, String productName, String salesPrice) {
                this.productId = productId;
                this.productName = productName;
                this.salesPrice = salesPrice;
            }
        }
    }

}
