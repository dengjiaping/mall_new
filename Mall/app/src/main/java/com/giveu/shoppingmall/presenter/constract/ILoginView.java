package com.giveu.shoppingmall.presenter.constract;

import com.giveu.shoppingmall.base.IView;

/**
 * 暴露某个activity特有的处理view的方法
 */

public interface ILoginView extends IView {
		void onLoginSuccess();
		void onLoginFail();

}
