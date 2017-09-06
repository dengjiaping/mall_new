package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionResponse extends BaseBean<CollectionResponse> {
    /**
     * pageNum : 1
     * pageSize : 10
     * pages : 10
     * resultList : [{"isPromotion":1,"keywords":"好用","monthAmount":"100.01","name":"碧欧泉Biotherm 基因活颜抗皱精华 50ml/1.69oz Skin Vivo Reversi","salePrice":"400.00","skuCode":"MES201609010289341","src":"img/app/order/icon_mianxi--b893377ec5.png","srcIp":"http://10.10.11.139/"}]
     */

    public int pageNum;
    public int pageSize;
    public int pages;
    public List<ResultListBean> resultList;

    public static class ResultListBean {
        /**
         * isPromotion : 1
         * keywords : 好用
         * monthAmount : 100.01
         * name : 碧欧泉Biotherm 基因活颜抗皱精华 50ml/1.69oz Skin Vivo Reversi
         * salePrice : 400.00
         * skuCode : MES201609010289341
         * src : img/app/order/icon_mianxi--b893377ec5.png
         * srcIp : http://10.10.11.139/
         */

        public int isPromotion;
        public String keywords;
        public String monthAmount;
        public String name;
        public String salePrice;
        public String skuCode;
        public String src;
        public String srcIp;
        public boolean isCheck = false;//单项是否选中,默认未选中
        public boolean isShowCb = false;//每一项前面是否显示框，默认不显示
    }

}
