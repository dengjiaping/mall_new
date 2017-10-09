package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
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
                    } else {
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
        ApiImpl.queryCommodityStock(null, province, city, region, skuCode, new BaseRequestAgent.ResponseListener<BaseBean>() {
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

    public void getAppDownPayAndMonthPay(String channel, String idPerson, int downPaymentRate, String skuCode, int quantity) {
        ApiImpl.getAppDownPayAndMonthPay(getView().getAct(), channel, idPerson, downPaymentRate, skuCode, quantity, new BaseRequestAgent.ResponseListener<DownPayMonthPayResponse>() {
            @Override
            public void onSuccess(DownPayMonthPayResponse response) {
                if (getView() != null) {
                    getView().showDownPayMonthPay(true, response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    getView().showDownPayMonthPay(true, null);
                    CommonLoadingView.showErrorToast(errorBean);
                }
            }
        });

    }

    public void getAppMonthlySupply(String channel, String idPerson, int downPaymentRate, long idProduct, int insuranceFee, String skuCode, int quantity) {
        ApiImpl.getAppMonthlySupply(getView().getAct(), channel, idPerson, downPaymentRate, idProduct, insuranceFee, quantity, skuCode, new BaseRequestAgent.ResponseListener<MonthSupplyResponse>() {
            @Override
            public void onSuccess(MonthSupplyResponse response) {
                if (getView() != null) {
                    getView().showAppMonthlySupply(response.data);
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
