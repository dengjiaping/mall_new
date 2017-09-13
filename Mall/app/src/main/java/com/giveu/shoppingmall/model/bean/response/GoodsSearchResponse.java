package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 524202 on 2017/9/7.
 */

public class GoodsSearchResponse extends BaseBean<GoodsSearchResponse> {

    /**
     * pageNum : 0
     * pageSize : 10
     * pages : 10
     * srcIp :"srcIp": "http://fastdfs.dafysz.cn"
     *
     * resultList : [{"src":"/enqile_m/img/app/order/icon_mianxi--b893377ec5.png","isPromotion":1,"name":"碧欧泉Biotherm 基因活颜抗皱精华 50ml/1.69oz Skin Vivo Reversi","skuCode":"MES201609010289341","isFullsub":11736,"monthAmount":100,"salePrice":400,"keywords":"无 50ml/1.69oz 走秀网络科技有限公司","srcIp":"http://10.10.11.139/","isInstallments":36548}]
     */

    public int pageNum;
    public int pageSize;
    public int pages;
    public String srcIp;
    public List<GoodsBean> resultList;

    public static class GoodsBean {
        /**
         * src : /enqile_m/img/app/order/icon_mianxi--b893377ec5.png
         * isPromotion : 1
         * name : 碧欧泉Biotherm 基因活颜抗皱精华 50ml/1.69oz Skin Vivo Reversi
         * skuCode : MES201609010289341
         * isFullsub : 11736
         * monthAmount : 100
         * salePrice : 400
         * keywords : 无 50ml/1.69oz 走秀网络科技有限公司
         * srcIp : http://10.10.11.139/
         * isInstallments : 36548
         */

        public String src;
        public int isPromotion;
        public String name;
        public String skuCode;
        public String isFullsub;
        public String monthAmount;
        public String salePrice;
        public String keywords;
        public String srcIp;
        public int isInstallments;
    }
}
