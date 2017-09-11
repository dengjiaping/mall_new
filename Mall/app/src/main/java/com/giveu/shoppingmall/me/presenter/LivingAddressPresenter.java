package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ILivingAddressView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressBean;
import com.giveu.shoppingmall.model.bean.response.LivingAddressBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DateUtil;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

/**
 * Created by 513419 on 2017/8/9.
 */

public class LivingAddressPresenter extends BasePresenter<ILivingAddressView> {

    public LivingAddressPresenter(ILivingAddressView view) {
        super(view);
    }

    public void addLiveAddress(String idPerson, String phone, String name,String email, String province, String city, String region, String street, String building) {
        ApiImpl.addLiveAddress(getView().getAct(), idPerson, phone, name,email, province, city, region, street, building, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().addSuccess();
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

    public void getAddListJson() {
        ApiImpl.getAddListJson(getView().getAct(), new BaseRequestAgent.ResponseListener<AddressBean>() {
            @Override
            public void onSuccess(final AddressBean response) {
                if (getView() != null && CommonUtils.isNotNullOrEmpty(response.data)) {
                    getView().getAddListJsonSuccess(response.data);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();

                            //本地缓存地址
                            StringUtils.saveAddress(getView().getAct(), gson.toJson(response.data));
                            //缓存地址时间
                            SharePrefUtil.getInstance().putString(Const.ADDRESS_TIME, DateUtil.getCurrentTime2yyyyMMdd());
                        }
                    }).start();

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


    public void getLiveAddress(String idPerson) {
        ApiImpl.getLiveAddress(getView().getAct(), idPerson, new BaseRequestAgent.ResponseListener<LivingAddressBean>() {
            @Override
            public void onSuccess(LivingAddressBean response) {
                if (getView() != null && response.data != null) {
                    getView().getLiveAddressSuccess(response.data);
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
