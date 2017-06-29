package com.giveu.shoppingmall.me.presenter;


import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IRegisterView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/20.
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    public RegisterPresenter(IRegisterView view) {
        super(view);
    }


    /**
     * 发送验证码
     * @param phone
     */
    public void sendSMSCode(String phone) {
        ApiImpl.sendSMSCode(getView().getAct(), phone, new BaseRequestAgent.ResponseListener<BaseBean>() {
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

    /**
     * 校验验证码
     * @param phone
     * @param code
     */
    public void checkSMSCode(String phone, String code) {
        ApiImpl.chkValiCode(getView().getAct(), code, phone, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().checkSMSSuccess();
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
