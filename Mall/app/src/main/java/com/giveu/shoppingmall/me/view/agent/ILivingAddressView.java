package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.AddressBean;
import com.giveu.shoppingmall.model.bean.response.LivingAddressBean;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/8/9.
 */

public interface ILivingAddressView extends IView {
    void addSuccess();
    void getAddListJsonSuccess(ArrayList<AddressBean> addressList);
    void getLiveAddressSuccess(LivingAddressBean data);
}
