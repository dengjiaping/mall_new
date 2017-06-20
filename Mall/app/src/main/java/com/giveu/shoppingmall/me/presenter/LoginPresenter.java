package com.giveu.shoppingmall.me.presenter;

import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.contract.LoginContract;

/**
 * Created by 513419 on 2017/6/20.
 */
public class LoginPresenter extends BasePresenter<LoginContract.LoginView> implements LoginContract.Presenter {
    protected LoginPresenter(LoginContract.LoginView view) {
        super(view);
    }
}
