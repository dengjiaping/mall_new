package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.json.JSONObject;

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

    public void getAddressList(String idPerson, String addressType) {
        ApiImpl.getAddressList(null, idPerson, addressType, new BaseRequestAgent.ResponseListener<AddressListResponse>() {
            @Override
            public void onSuccess(AddressListResponse response) {
                if (getView() != null && response != null && response.data != null) {
                    if (response.data.size() > 0) {
                        AddressListResponse addressBean = response.data.get(0);
                        getView().getAddressList(true, addressBean.province, addressBean.city, addressBean.region);
                    }else {
                        getView().getAddressList(false, "", "", "");
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    getView().getAddressList(false, "", "", "");
                }
            }
        });
    }

    public void queryCommodityStock(String province, String city, final String region, String skuCode) {
        ApiImpl.queryCommodityStock(getView().getAct(), province, city, region, skuCode, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    try {
                        int stock = new JSONObject(response.originResultString).getInt("data");
                        getView().showStockState(stock);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }

}
