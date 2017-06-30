package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.me.view.agent.IRequestPwdView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CheckSmsResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class RequestPwdPresenter extends SendSmsPresenter<IRequestPwdView> {
    public RequestPwdPresenter(IRequestPwdView view) {
        super(view);
    }

    public void checkSms(String mobile, String smsCode) {
        ApiImpl.checkSmsCode(getView().getAct(), mobile, smsCode, new BaseRequestAgent.ResponseListener<CheckSmsResponse>() {
            @Override
            public void onSuccess(CheckSmsResponse response) {
                if (getView() != null) {
                    if (response.data.skip) {
                        getView().skipToChangePassword(response.data.randCode);
                    } else {
                        getView().skipToIdentify(response.data.randCode);
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
