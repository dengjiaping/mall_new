package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.CommodityDetailResponse;

/**
 * Created by 513419 on 2017/9/7.
 */

public interface ICommodityDetailView  extends IView{
    void showCommodity(CommodityDetailResponse data);
}
