package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;

/**
 * Created by 513419 on 2017/6/20.
 */

public interface ILoginView extends IView {
    void onLoginSuccess(LoginResponse data);
    void onLoginFail();
    void afterThirdLogin();
}
