package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.model.bean.response.RepaymentResponse;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/3.
 */

public interface IRepaymentView extends IView {
    void showRepayment(RepaymentResponse.HeaderBean headerBean, ArrayList<RepaymentBean> currentMonthList, ArrayList<RepaymentBean> nextMonthList);
    void showEmpty();
}
