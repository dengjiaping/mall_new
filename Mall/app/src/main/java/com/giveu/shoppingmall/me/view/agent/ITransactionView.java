package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/6/26.
 */

public interface ITransactionView extends IView {
    void showContractResult(ArrayList<ContractResponse.Contract> contractList);
}
