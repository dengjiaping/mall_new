package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ISendSmsView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/30.
 * 发送短信的presenter
 */

public class SendSmsPresenter<T extends ISendSmsView> extends BasePresenter<T> {

    public SendSmsPresenter(T view) {
        super(view);
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    public void sendSMSCode(String phone, String codeType) {
        ApiImpl.sendSMSCode(getView().getAct(), phone, codeType, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().sendSMSSuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
