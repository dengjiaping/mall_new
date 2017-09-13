package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/9/8.
 */

public class CommodityPresenter extends BasePresenter<ICommodityView> {
    public CommodityPresenter(ICommodityView view) {
        super(view);
    }

    public void collectCommodity(String idPerson, String skuCode, int status) {
        ArrayList<String> skuCodes = new ArrayList<>();
        skuCodes.add(skuCode);
        ApiImpl.deleteCollection(getView().getAct(), idPerson, skuCodes, status, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().collectOperator();
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


    public void getCommodityDetail(String channel, String skuCode) {
        ApiImpl.getCommodityDetail(null, skuCode, channel, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null && response.data != null) {
                    getView().showCommodity(response.data.toString());
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }
}
