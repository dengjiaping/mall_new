package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityDetailView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CommodityDetailResponse;

/**
 * Created by 513419 on 2017/9/7.
 */

public class CommodityDetailPresenter extends BasePresenter<ICommodityDetailView> {
    public CommodityDetailPresenter(ICommodityDetailView view) {
        super(view);
    }

    public void getCommodityDetail(String skuCode) {
        ApiImpl.getCommodityDetail(null, skuCode, new BaseRequestAgent.ResponseListener<CommodityDetailResponse>() {
            @Override
            public void onSuccess(CommodityDetailResponse response) {
                if (getView() != null && response.data != null) {
                    getView().showCommodity(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }
}
