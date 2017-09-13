package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/9/12.
 */

public class IndexResponse extends BaseBean<ArrayList<IndexResponse>> {

    /**
     * modelName :
     * typeValue : 0
     * decorations : [{"name":null,"code":"123456","title":null,"iconSrc":"","picSrc":"group1/M11/5A/FB/CgsLw1mSMk6AaK_lAAMaxoCu6as932.jpg","url":"/v1/sc/goods/decoration/search","urlTypeValue":"0"},{"name":null,"code":"23454","title":null,"iconSrc":"","picSrc":"group1/M0F/5A/FC/CgsLw1mSMlOAbrGOAACzCIwKa8s993.jpg","url":"/v1/sc/goods/decoration/search","urlTypeValue":"0"},{"name":null,"code":"123133","title":null,"iconSrc":"","picSrc":"group1/M02/4E/8B/CgsLxVmSypKAPbKqAABRa-Mz4XE919.jpg","url":"H5","urlTypeValue":"1"},{"name":null,"code":"3242","title":null,"iconSrc":"","picSrc":"group1/M04/78/3D/CgsLw1mSyn2AL-6eAAM6otB8uds346.jpg","url":"H5","urlTypeValue":"1"}]
     * srcIp : http://fastdfs.dafysz.cn
     */

    public String modelName;
    public String typeValue;
    public String srcIp;
    public List<DecorationsBean> decorations;

    public static class DecorationsBean {
        /**
         * name : null
         * code : 123456
         * title : null
         * iconSrc :
         * picSrc : group1/M11/5A/FB/CgsLw1mSMk6AaK_lAAMaxoCu6as932.jpg
         * url : /v1/sc/goods/decoration/search
         * urlTypeValue : 0
         */
        public String name;
        public String title;
        public String iconSrc;
        public String picSrc;
        public String code;
        public String url;
        public String urlTypeValue;
    }
}
