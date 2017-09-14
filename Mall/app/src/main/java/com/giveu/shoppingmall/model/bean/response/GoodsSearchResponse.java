package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 524202 on 2017/9/7.
 */

public class GoodsSearchResponse extends BaseBean<GoodsSearchResponse> {


    /**
     * pageNum : 1
     * pageSize : 10
     * pages : 1
     * srcIp : http://fastdfs.dafysz.cn
     * resultList : [{"keywords":"小米不仅仅大","monthAmount":"0.0","name":"小米5s 全网通 高配版 3GB内存 64GB ROM 哑光金 移动联通电信4G手机","salePrice":"1999.0","skuCode":"K00002735","isInstallments":1,"src":"group1/M0D/6D/7E/CgsLw1mSiDSAMqF4AAPGOtetOv8730.jpg","isPromotion":null}]
     */

    public int pageNum;
    public int pageSize;
    public int pages;
    public String srcIp;
    public List<GoodsBean> resultList;


    public static class GoodsBean {
        /**
         * keywords : 小米不仅仅大
         * monthAmount : 0.0
         * name : 小米5s 全网通 高配版 3GB内存 64GB ROM 哑光金 移动联通电信4G手机
         * salePrice : 1999.0
         * skuCode : K00002735
         * isInstallments : 1
         * src : group1/M0D/6D/7E/CgsLw1mSiDSAMqF4AAPGOtetOv8730.jpg
         * isPromotion : null
         */

        public String keywords;
        public String monthAmount;
        public String name;
        public String salePrice;
        public String skuCode;
        public int isInstallments;
        public String src;
        public String isPromotion;
        /**
         * 是否显示月供
         *
         * @return
         */
        public boolean hasShowMonthAmount() {
            //isInstallments 0分期不显示月供
            if (0 == isInstallments) {
               return false;
            }
            return true;
        }
    }
}
