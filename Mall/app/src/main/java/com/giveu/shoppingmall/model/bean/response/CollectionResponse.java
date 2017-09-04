package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionResponse extends BaseBean<CollectionResponse> {
    public boolean isShowCb;//每一项前面是否显示框
    public boolean isCheck;//单项是否选中
    public String test;//测试

    public CollectionResponse(boolean isShowCb, boolean isCheck, String test) {
        this.isShowCb = isShowCb;
        this.isCheck = isCheck;
        this.test = test;
    }
}
