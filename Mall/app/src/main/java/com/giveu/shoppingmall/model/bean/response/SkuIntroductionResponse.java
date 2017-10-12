package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/9/7.
 */

public class SkuIntroductionResponse extends BaseBean<SkuIntroductionResponse> {

    /**
     * collectStatus : 87561
     * skuInfo : {"adwords":"双镜头  大内存  长续航！","downPaymentRate":0,"freeInterest":0,"isInstallments":1,"isPromotion":1,"monthAmount":"124.00","name":"荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金","periods":31378,"salePrice":"1,299.00","serviceSafeguards":[{"detail":"商品供货由京东提供，保证正品货源","name":"正品保证","src":"http://fastdfs.dafysz.cnnull"},{"detail":"商品发货由京东提供售后物流，下单后将优先发货","name":"急速发货","src":"http://fastdfs.dafysz.cnnull"},{"detail":"全场满99元即可包邮，无需额外垫付运费","name":"满99元包邮","src":"http://fastdfs.dafysz.cnnull"}],"skuCode":"K00002719","src":"group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg","srcIp":"http://fastdfs.dafysz.cn","srcs":["http://fastdfs.dafysz.cn/group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg","http://fastdfs.dafysz.cn/group1/M12/65/E5/CgsLw1mSb3GAUxe3AAPJCw09Ra0319.jpg","http://fastdfs.dafysz.cn/group1/M07/65/E5/CgsLw1mSb3KAapeNAATTJsMPl8g017.jpg","http://fastdfs.dafysz.cn/group1/M08/3C/39/CgsLxVmSb5yALW9VAAJTEgdM6OQ945.jpg","http://fastdfs.dafysz.cn/group1/M08/69/5F/CgsLxFmSb8SAT-mbAADrcEMfYxk698.jpg","http://fastdfs.dafysz.cn/group1/M08/65/EF/CgsLw1mSb5KAP6MSAAC7Kb21OYY794.jpg"],"supplier":"京东"}
     * skuSpecs : [{"specValues":[{"skuCodes":["K00002719"],"skuIds":[40],"specValue":"全网通（4GB 32GB）"}],"name":"版本","specId":204},{"specValues":[{"skuCodes":["K00002719"],"skuIds":[40],"specValue":"全网通（3GB 32GB）"}],"name":"选择套装","specId":206},{"specValues":[{"skuCodes":["K00002719"],"skuIds":[40],"specValue":"全网通（4GB 32GB）"}],"name":"颜色","specId":208}]
     */

    public int collectStatus;
    public SkuInfoBean skuInfo;
    public List<SkuSpecsBean> skuSpecs;

    public static class SkuInfoBean {
        /**
         * adwords : 双镜头  大内存  长续航！
         * downPaymentRate : 0
         * freeInterest : 0
         * isInstallments : 1
         * isPromotion : 1
         * monthAmount : 124.00
         * name : 荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金
         * periods : 31378
         * salePrice : 1,299.00
         * serviceSafeguards : [{"detail":"商品供货由京东提供，保证正品货源","name":"正品保证","src":"http://fastdfs.dafysz.cnnull"},{"detail":"商品发货由京东提供售后物流，下单后将优先发货","name":"急速发货","src":"http://fastdfs.dafysz.cnnull"},{"detail":"全场满99元即可包邮，无需额外垫付运费","name":"满99元包邮","src":"http://fastdfs.dafysz.cnnull"}]
         * skuCode : K00002719
         * src : group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg
         * srcIp : http://fastdfs.dafysz.cn
         * srcs : ["http://fastdfs.dafysz.cn/group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg","http://fastdfs.dafysz.cn/group1/M12/65/E5/CgsLw1mSb3GAUxe3AAPJCw09Ra0319.jpg","http://fastdfs.dafysz.cn/group1/M07/65/E5/CgsLw1mSb3KAapeNAATTJsMPl8g017.jpg","http://fastdfs.dafysz.cn/group1/M08/3C/39/CgsLxVmSb5yALW9VAAJTEgdM6OQ945.jpg","http://fastdfs.dafysz.cn/group1/M08/69/5F/CgsLxFmSb8SAT-mbAADrcEMfYxk698.jpg","http://fastdfs.dafysz.cn/group1/M08/65/EF/CgsLw1mSb5KAP6MSAAC7Kb21OYY794.jpg"]
         * supplier : 京东
         */

        public String adwords;
        public int downPaymentRate;
        public int freeInterest;
        public int isInstallments;
        public int isPromotion;
        public String monthAmount;
        public String name;
        public int periods;
        public String salePrice;
        public String skuCode;
        public String src;
        public String srcIp;
        public String supplier;
        public List<String> serviceSafeguards;
        public List<String> srcs;

        //是否分期
        public boolean isCredit() {
            if (1 == isInstallments) {
                return true;
            }
            return false;
        }


    }

    public static class ServiceSafeguardsBean {
        /**
         * detail : 商品供货由京东提供，保证正品货源
         * name : 正品保证
         * src : http://fastdfs.dafysz.cnnull
         */

        public String detail;
        public String name;
        public String src;
    }

    public static class SkuSpecsBean {
        /**
         * specValues : [{"skuCodes":["K00002719"],"skuIds":[40],"specValue":"全网通（4GB 32GB）"}]
         * name : 版本
         * specId : 204
         */

        public String name;
        public int specId;
        public List<SpecValuesBean> specValues;


    }

    public static class SpecValuesBean {
        /**
         * skuCodes : ["K00002719"]
         * skuIds : [40]
         * specValue : 全网通（4GB 32GB）
         */

        public String specValue;
        public List<String> skuCodes;
        public List<Integer> skuIds;
    }
}
