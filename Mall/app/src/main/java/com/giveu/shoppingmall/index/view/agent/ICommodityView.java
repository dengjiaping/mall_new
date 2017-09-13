package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.CommodityDetailResponse;

/**
 * Created by 513419 on 2017/9/8.
 */

public interface ICommodityView extends IView {
    void collectOperator();
    void showCommodity(CommodityDetailResponse data);
}
