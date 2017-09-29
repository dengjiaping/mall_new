package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/8/10.
 */

public class Province extends BaseBean<ArrayList<Province>>{


    public String name;
    public List<ArrayCity> array;

    public static class ArrayCity {
        public String name;
        public List<ArrayRegion> array;


    }
    public static class ArrayRegion {
        public String name;
        public List<String> array;
    }
}
