package com.giveu.shoppingmall.presenter;

import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.presenter.constract.ILoginView;

/**
 * Created by 508632 on 2016/12/20.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

	public LoginPresenter(ILoginView iView) {
		super(iView);
	}

	public void Login(String name, String password) {
//		ApiImpl.getInstance().testRequestAgent();
		getViewProxy().onLoginFail();
		getViewProxy().onLoginSuccess();
	}





}
