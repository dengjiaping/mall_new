package com.giveu.shoppingmall.me.presenter;


import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.me.view.agent.IRegisterView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 513419 on 2017/6/20.
 */
public class RegisterPresenter extends SendSmsPresenter<IRegisterView> {


    public RegisterPresenter(IRegisterView view) {
        super(view);
    }

    public void registerFirst(String phone, String code) {
        ApiImpl.registerFirst(getView().getAct(), phone, code, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    String jsonResult = response.data.toString();
                    if (StringUtils.isNotNull(jsonResult)) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResult);
                            getView().registerFirstSuccess(jsonObject.getString("randCode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
