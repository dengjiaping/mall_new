package com.giveu.shoppingmall.me.view.agent;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.base.IView;

import java.util.List;


/**
 * Created by 101912 on 2017/8/30.
 */

public interface IOrderInfoView<T extends BaseBean> extends IView {

    void showOrderDetail(T response);

    void deleteOrderSuccess(String orderNo);

    void cancelOrderSuccess(String orderNo);

    void confirmReceiveSuccess(String orderNo);

}
