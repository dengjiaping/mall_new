package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/9/7.
 */

public class CommodityInfoPresenter extends BasePresenter<ICommodityInfoView> {

    public CommodityInfoPresenter(ICommodityInfoView view) {
        super(view);
    }

    public void getSkuIntroduce(String idPerson, String channel, String skuCode) {
        ApiImpl.getSkuIntroduction(getView().getAct(), idPerson, channel, skuCode, new BaseRequestAgent.ResponseListener<SkuIntroductionResponse>() {
            @Override
            public void onSuccess(SkuIntroductionResponse response) {
                if (getView() != null && response.data != null) {
                    getView().showSkuIntroduction(response.data);
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    CommonLoadingView.showErrorToast(errorBean);
                }
            }
        });
    }
}
