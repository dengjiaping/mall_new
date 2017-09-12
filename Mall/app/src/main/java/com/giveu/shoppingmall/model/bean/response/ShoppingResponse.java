package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/9/12.
 */

public class ShoppingResponse extends BaseBean<ShoppingResponse> {

    /**
     * srcIp : http://fastdfs.dafysz.cn
     * pages : 2
     * pageSize : 10
     * pageNum : 1
     * resultList : [{"keywords":"荣耀,华为,手机","monthAmount":"124.00","name":"荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金","salePrice":"1,299.00","skuCode":"K00002719","isInstallments":1,"src":"group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg","isPromotion":1},{"keywords":"手机,小米","monthAmount":"160.00","name":"小米Max2 全网通 4GB+64GB 金色 移动联通电信4G手机 双卡双待","salePrice":"1,669.00","skuCode":"K00002733","isInstallments":1,"src":"group1/M10/6D/5B/CgsLw1mSh6KAebBeAAE1bwEvgwI737.jpg","isPromotion":1},{"keywords":"手机,华为,畅想","monthAmount":"86.00","name":"华为 畅享6 金色 移动联通电信4G手机 双卡双待","salePrice":"899.00","skuCode":"K00002718","isInstallments":1,"src":"group1/M11/3B/F7/CgsLxVmSbteASJpsAAUDAh955Go248.jpg","isPromotion":0},{"keywords":"苹果,手机,iPhone","monthAmount":"341.00","name":"Apple iPhone 7 Plus (A1661) 128G 黑色 移动联通电信4G手机","salePrice":"6,299.00","skuCode":"K00002857","isInstallments":1,"src":"group1/M0C/BB/63/CgsLxFmUQAKAeZ2QAAIJb733Hlc459.jpg","isPromotion":0},{"keywords":"iphone7,红色","monthAmount":"287.00","name":"Apple iPhone 7 (A1660) 128G 黑色 移动联通电信4G手机","salePrice":"5,299.00","skuCode":"K00002702","isInstallments":1,"src":"group1/M07/17/7A/CgsLxVmRoKSAStTdAAIIYD7F3xQ614.jpg","isPromotion":0},{"keywords":"oppo","monthAmount":"195.00","name":"OPPO R11 Plus 6GB+64GB内存版 全网通4G手机 双卡双待 玫瑰金色","salePrice":"3,600.00","skuCode":"K00002723","isInstallments":1,"src":"group1/M0F/6D/B2/CgsLw1mSiQ2AFMxsAAK1UfSjVQs554.jpg","isPromotion":0},{"keywords":"荣耀,手机,华为","monthAmount":"287.00","name":"荣耀 V9 全网通 高配版 6GB+64GB 极光蓝 移动联通电信4G手机 双卡双待","salePrice":"2,999.00","skuCode":"K00002726","isInstallments":1,"src":"group1/M0C/67/E9/CgsLxFmSa26ANV4mAAVU7f0xRxk731.jpg","isPromotion":0},{"keywords":"荣耀,手机","monthAmount":"77.00","name":"荣耀 畅玩6A 2GB+16GB 银色 全网通4G手机 双卡双待","salePrice":"799.00","skuCode":"K00002729","isInstallments":1,"src":"group1/M09/65/6C/CgsLw1mSbgmARd2QAAVfH05J87E312.jpg","isPromotion":0},{"keywords":"手机,oppo","monthAmount":"249.00","name":"OPPO R9s 全网通4G+64G 双卡双待手机 金色","salePrice":"2,599.00","skuCode":"K00002737","isInstallments":1,"src":"group1/M14/6D/96/CgsLw1mSiJmAMM3TAAGSdKjtf-E709.jpg","isPromotion":0},{"keywords":"手机","monthAmount":"86.00","name":"乐视（LeEco）乐2 全网通（Le X520）3GB+32GB 原力金 移动联通电信4G手机 双卡双待","salePrice":"899.00","skuCode":"K00002732","isInstallments":1,"src":"group1/M12/43/9B/CgsLxVmSh4KASDERAAMBvft15vk832.jpg","isPromotion":0}]
     */

    public String srcIp;
    public int pages;
    public int pageSize;
    public int pageNum;
    public List<ResultListBean> resultList;

    public static class ResultListBean {
        /**
         * keywords : 荣耀,华为,手机
         * monthAmount : 124.00
         * name : 荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金
         * salePrice : 1,299.00
         * skuCode : K00002719
         * isInstallments : 1
         * src : group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg
         * isPromotion : 1
         */

        public String keywords;
        public String monthAmount;
        public String name;
        public String salePrice;
        public String skuCode;
        public int isInstallments;
        public String src;
        public int isPromotion;
    }
}
