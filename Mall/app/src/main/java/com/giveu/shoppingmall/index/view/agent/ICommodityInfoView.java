package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/9/7.
 */

public interface ICommodityInfoView extends IView {
    void showSkuIntroduction(SkuIntroductionResponse skuResponse);

    void showStockState(int state);

    void getAddressList(boolean hasAddress, String province, String city, String region);

    void showDownPayMonthPay(boolean success, ArrayList<DownPayMonthPayResponse> data);
}
