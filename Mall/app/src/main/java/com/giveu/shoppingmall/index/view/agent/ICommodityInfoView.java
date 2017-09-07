package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;

/**
 * Created by 513419 on 2017/9/7.
 */

public interface ICommodityInfoView extends IView{
    void showSkuIntroduction(SkuIntroductionResponse skuResponse);
}
