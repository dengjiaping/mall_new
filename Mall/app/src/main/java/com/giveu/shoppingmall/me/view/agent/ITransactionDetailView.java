package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.TransactionDetailResponse;

/**
 * Created by 513419 on 2017/7/4.
 */

public interface ITransactionDetailView extends IView {
    void showTransactionDetail(TransactionDetailResponse data);
}
