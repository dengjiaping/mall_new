package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/3.
 */

public interface IBillIistView extends IView {
    void showBillList(BillListResponse.HeaderBean headerBean, ArrayList<BillBean> currentMonthList, ArrayList<BillBean> nextMonthList);
}
