package com.giveu.shoppingmall.me.view.agent;

/**
 * Created by 513419 on 2017/6/26.
 */

public interface IRequestPwdView extends ISendSmsView {
    void skipToIdentify(String randCode);
    void skipToChangePassword(String randCode);
}
