package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;

/**
 * Created by 513419 on 2017/6/26.
 */

public interface ISetPasswordView extends IView{
    void registerSuccess(RegisterResponse response);
    void changePwdSuccess();
}
