package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionResponse extends BaseBean<CollectionResponse> {
    public boolean isShowCb;

    public CollectionResponse(boolean isShowCb) {
        this.isShowCb = isShowCb;
    }
}
