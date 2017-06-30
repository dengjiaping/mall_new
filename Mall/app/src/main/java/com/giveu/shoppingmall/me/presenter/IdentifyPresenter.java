package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IIdentifyView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/30.
 */

public class IdentifyPresenter extends BasePresenter<IIdentifyView> {
    public IdentifyPresenter(IIdentifyView view) {
        super(view);
    }

    public void checkUserInfo(String certNo, String mobile, String randCode, String userName) {
        ApiImpl.checkUserInfo(getView().getAct(), certNo, mobile, randCode, userName, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().checkSuccess(response.data.toString());
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
