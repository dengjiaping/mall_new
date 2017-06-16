package com.giveu.shoppingmall.utils.listener;

import com.giveu.shoppingmall.base.BaseListener;

/**
 * Created by 508632 on 2017/2/4.
 */

public interface SuccessOrFailListener extends BaseListener {
	void onSuccess(Object o);
	void onFail(Object o);
}
