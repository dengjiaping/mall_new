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
     * resultList : [{"src":"/enqile_m/img/app/order/icon_mianxi--b893377ec5.png","isPromotion":1,"name":"碧欧泉Biotherm 基因活颜抗皱精华 50ml/1.69oz Skin Vivo Reversi","skuCode":"MES201609010289341","isFullsub":11736,"monthAmount":100,"salePrice":400,"keywords":"无 50ml/1.69oz 走秀网络科技有限公司","srcIp":"http://10.10.11.139/","isInstallments":36548}]
     */

    private int pageNum;
    private int pageSize;
    private int pages;
    private List<GoodsBean> resultList;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<GoodsBean> getGoodsList() {
        return resultList;
    }

    public void setGoodsList(List<GoodsBean> resultList) {
        this.resultList = resultList;
    }

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

        private String src;
        private int isPromotion;
        private String name;
        private String skuCode;
        private int isFullsub;
        private int monthAmount;
        private int salePrice;
        private String keywords;
        private String srcIp;
        private int isInstallments;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public int getIsPromotion() {
            return isPromotion;
        }

        public void setIsPromotion(int isPromotion) {
            this.isPromotion = isPromotion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSkuCode() {
            return skuCode;
        }

        public void setSkuCode(String skuCode) {
            this.skuCode = skuCode;
        }

        public int getIsFullsub() {
            return isFullsub;
        }

        public void setIsFullsub(int isFullsub) {
            this.isFullsub = isFullsub;
        }

        public int getMonthAmount() {
            return monthAmount;
        }

        public void setMonthAmount(int monthAmount) {
            this.monthAmount = monthAmount;
        }

        public int getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(int salePrice) {
            this.salePrice = salePrice;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getSrcIp() {
            return srcIp;
        }

        public void setSrcIp(String srcIp) {
            this.srcIp = srcIp;
        }

        public int getIsInstallments() {
            return isInstallments;
        }

        public void setIsInstallments(int isInstallments) {
            this.isInstallments = isInstallments;
        }
    }
}
