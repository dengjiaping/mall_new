package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;

import java.util.List;

/**
 * Created by 513419 on 2017/7/4.
 */

public interface ICreditDetailView extends IView{
    void showCreditDetail(List<ListInstalmentResponse.Instalment> instalmentList);
}
