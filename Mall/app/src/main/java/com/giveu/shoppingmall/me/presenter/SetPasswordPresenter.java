package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ISetPasswordView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class SetPasswordPresenter extends BasePresenter<ISetPasswordView> {
    public SetPasswordPresenter(ISetPasswordView view) {
        super(view);
    }

    public void register(String mobile, String password, String randCode) {
        ApiImpl.register(getView().getAct(), mobile, password, randCode, new BaseRequestAgent.ResponseListener<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse response) {
                if (getView() != null) {
                    getView().registerSuccess(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void resetPassword(String mobile, String password, String randCode, String userName) {
        ApiImpl.resetPassword(getView().getAct(), mobile, password, userName, randCode, new BaseRequestAgent.ResponseListener<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse response) {
                if (getView() != null) {
                    getView().changePwdSuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
